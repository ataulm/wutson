package com.ataulm.wutson.auth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.Log;
import com.ataulm.wutson.R;
import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.rx.LoggingObserver;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OAuthSignInActivity extends Activity {

    private WutsonAccountManager accountManager;
    private AccountAuthenticatorResponse authenticatorResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_web);

        accountManager = WutsonAccountManager.newInstance(this);
        authenticatorResponse = getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        authenticatorResponse.onRequestContinued();

        WebView webView = (WebView) findViewById(R.id.web);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String redirectUrl) {
                if (redirectUrl.startsWith(BuildConfig.TRAKT_REDIRECT_URI)) {
                    String code = extractCodeFromUrl(redirectUrl);
                    onRedirectWith(code);
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, redirectUrl);
                }
            }
        });

        String authorizationEndpoint = getIntent().getData().toString();
        webView.loadUrl(authorizationEndpoint);
    }

    private static String extractCodeFromUrl(String url) {
        String query = URI.create(url).getQuery();
        String[] pairs = query.split("&");
        Map<String, String> queryParams = new HashMap<>(pairs.length);
        for (String pair : pairs) {
            int index = pair.indexOf("=");
            try {
                queryParams.put(URLDecoder.decode(pair.substring(0, index), "UTF-8"), URLDecoder.decode(pair.substring(index + 1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Couldn't extract code param from URL: " + url, e);
            }
        }
        return queryParams.get("code");
    }

    private void onRedirectWith(String code) {
        new TraktOAuthTokenRequester()
                .getAccessTokenInExchangeFor(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer(Jabber.log()));
        // TODO: no unsubscriptions??? cray
    }

    private class Observer extends LoggingObserver<TraktOAuthTokenResponse> {

        public Observer(Log log) {
            super(log);
        }

        @Override
        public void onNext(TraktOAuthTokenResponse foo) {
            super.onNext(foo);

            // TODO: should fetch the username from the Trakt api before making the account
            Account account = new Account(getString(R.string.account_name), getString(R.string.account_type));
            AccessToken accessToken = AccessToken.from(foo);
            RefreshToken refreshToken = new RefreshToken(foo.refreshToken);

            accountManager.addAccount(account, accessToken, refreshToken);

            Intent intent = new Intent();
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            intent.putExtra(AccountManager.KEY_AUTHTOKEN, accessToken.toString());

            setResult(RESULT_OK, intent);
            authenticatorResponse.onResult(intent.getExtras());
            finish();
        }

    }

}
