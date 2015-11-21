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
        if (accountManager.getRefreshToken().isEmpty()) {
            return bundleThatWillPromptUserLogin(response);
        }

        if (accountManager.needToRefreshAccessToken()) {
            AccessToken newAccessToken = fetchNewAccessToken();
            accountManager.setAuthToken(account, newAccessToken);
            return authTokenBundleFrom(account, newAccessToken);
        }

        return authTokenBundleFrom(account);
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

    private AccessToken fetchNewAccessToken() {
        RefreshToken refreshToken = accountManager.getRefreshToken();
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

    private Bundle authTokenBundleFrom(Account account) {
        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);

        AccessToken accessToken = accountManager.getAccessToken();
        bundle.putString(AccountManager.KEY_AUTHTOKEN, accessToken.toString());
        return bundle;
    }

}
