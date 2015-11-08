package com.ataulm.wutson.auth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.ataulm.wutson.BuildConfig;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import rx.functions.Func1;

public class WutsonAuthenticator extends SimpleAbstractAccountAuthenticator {

    private static final Uri AUTH_URI = Uri.parse("https://api-v2launch.trakt.tv/oauth/authorize" +
            "?response_type=code" +
            "&client_id=" + BuildConfig.TRAKT_API_KEY +
            "&redirect_uri=" + BuildConfig.TRAKT_REDIRECT_URI);

    private final Context context;
    private final WutsonAccountManager accountManager;

    public WutsonAuthenticator(Context context) {
        super(context);
        this.context = context.getApplicationContext();
        this.accountManager = WutsonAccountManager.newInstance(context);
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Bundle result = new Bundle();
        result.putParcelable(AccountManager.KEY_INTENT,
                new Intent(context, OAuthSignInActivity.class)
                        .putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
                        .setData(AUTH_URI)
        );
        return result;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        if (accountManager.isMissingRefreshTokenFor(account)) {
            return bundleThatWillPromptUserLogin(response);
        }

        if (accountManager.needToRefreshAccessToken(account, authTokenType)) {
            AccessToken refreshedToken = refreshAccessTokenResponseFor(account);
            accountManager.setAuthToken(account, refreshedToken);
            return authTokenBundleFrom(account, refreshedToken);
        }

        return authTokenBundleFrom(account, authTokenType);
    }

    private Bundle bundleThatWillPromptUserLogin(AccountAuthenticatorResponse response) {
        Bundle result = new Bundle();
        result.putParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        result.putParcelable(AccountManager.KEY_INTENT,
                new Intent(context, OAuthSignInActivity.class)
                        .putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
                        .setData(AUTH_URI)
        );
        return result;
    }

    private AccessToken refreshAccessTokenResponseFor(Account account) {
        RefreshToken refreshToken = accountManager.getRefreshTokenFor(account);
        return new TraktOAuthTokenRequester()
                .getAccessTokenInExchangeFor(refreshToken)
                .map(new Func1<TraktOAuthTokenResponse, AccessToken>() {

                    @Override
                    public AccessToken call(TraktOAuthTokenResponse response) {
                        return AccessToken.from(response);
                    }

                })
                .toBlocking()
                .first();
    }

    private Bundle authTokenBundleFrom(Account account, AccessToken token) {
        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        bundle.putString(AccountManager.KEY_AUTHTOKEN, token.toString());

        Bundle userData = bundleWithMetadataAbout(token);
        bundle.putBundle(AccountManager.KEY_USERDATA, userData);
        return bundle;
    }

    private Bundle bundleWithMetadataAbout(AccessToken token) {
        Bundle userData = new Bundle();
        userData.putLong(WutsonAccountManager.KEY_TOKEN_EXPIRY, token.getExpiry());
        return userData;
    }

    private Bundle authTokenBundleFrom(Account account, String authTokenType) {
        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);

        AccessToken accessToken = accountManager.getAccessTokenFor(account, authTokenType);
        bundle.putString(AccountManager.KEY_AUTHTOKEN, accessToken.toString());
        return bundle;
    }

}
