package com.ataulm.wutson.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.R;

import java.util.concurrent.TimeUnit;

public final class WutsonAccountManager {

    public static final String KEY_TOKEN_EXPIRY = BuildConfig.APPLICATION_ID + ".KEY_TOKEN_EXPIRY";

    private final AccountManager accountManager;
    private final String accountType;

    public static WutsonAccountManager newInstance(Context context) {
        AccountManager accountManager = AccountManager.get(context.getApplicationContext());
        String accountType = context.getString(R.string.account_type);
        return new WutsonAccountManager(accountManager, accountType);
    }

    private WutsonAccountManager(AccountManager accountManager, String accountType) {
        this.accountManager = accountManager;
        this.accountType = accountType;
    }

    public void setAuthToken(Account account, AccessToken accessToken) {
        accountManager.setAuthToken(account, account.type, accessToken.toString());
    }

    public boolean isMissingRefreshTokenFor(Account account) {
        return getRefreshTokenFor(account).isEmpty();
    }

    public RefreshToken getRefreshTokenFor(Account account) {
        String secretRefreshToken = accountManager.getPassword(account);
        return new RefreshToken(secretRefreshToken);
    }

    public boolean needToRefreshAccessToken(Account account, String authTokenType) {
        AccessToken accessToken = getAccessTokenFor(account, authTokenType);
        return accessToken.isEmpty() || TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) >= accessToken.getExpiry();
    }

    public AccessToken getAccessTokenFor(Account account, String authTokenType) {
        String token = accountManager.peekAuthToken(account, authTokenType);
        long tokenExpiry = Long.parseLong(accountManager.getUserData(account, KEY_TOKEN_EXPIRY));
        return new AccessToken(token, tokenExpiry);
    }

    public void addAccount(Account account, AccessToken accessToken, RefreshToken refreshToken) {
        Bundle userData = new Bundle();
        userData.putLong(WutsonAccountManager.KEY_TOKEN_EXPIRY, accessToken.getExpiry());
        accountManager.addAccountExplicitly(account, refreshToken.toString(), userData);
        accountManager.setAuthToken(account, account.type, accessToken.toString());
    }

    @Nullable
    public Account getAccount() {
        Account[] wutsonAccounts = accountManager.getAccountsByType(accountType);
        return wutsonAccounts.length == 0 ? null : wutsonAccounts[0];
    }

    public void startAddAccountProcess(Activity activity) {
        accountManager.addAccount(accountType, null, null, null, activity, null, null);
    }

}
