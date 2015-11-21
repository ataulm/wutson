package com.ataulm.wutson.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.R;

import java.util.concurrent.TimeUnit;

public final class WutsonAccountManager {

    public static final String KEY_TOKEN_EXPIRY = BuildConfig.APPLICATION_ID + ".KEY_TOKEN_EXPIRY";

    private final AccountManager accountManager;
    private final String accountType;
    private final String authTokenType;

    public static WutsonAccountManager newInstance(Context context) {
        AccountManager accountManager = AccountManager.get(context.getApplicationContext());
        String accountType = context.getString(R.string.account_type);
        String authTokenType = accountType; // RETRO: if I use something else, it doesn't work :/ getAccessToken returns null
        return new WutsonAccountManager(accountManager, accountType, authTokenType);
    }

    private WutsonAccountManager(AccountManager accountManager, String accountType, String authTokenType) {
        this.accountManager = accountManager;
        this.accountType = accountType;
        this.authTokenType = authTokenType;
    }

    public void startAddAccountProcess(Activity activity) {
        accountManager.addAccount(accountType, authTokenType, null, null, activity, null, null);
    }

    public void setAuthToken(Account account, AccessToken accessToken) {
        accountManager.setAuthToken(account, account.type, accessToken.toString());
    }

    public RefreshToken getRefreshToken() {
        Account account = getAccount();
        if (account == null) {
            return RefreshToken.EMPTY;
        }
        String secretRefreshToken = accountManager.getPassword(account);
        return new RefreshToken(secretRefreshToken);
    }

    public boolean needToRefreshAccessToken() {
        AccessToken accessToken = getAccessToken();
        return accessToken.isEmpty() || TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) >= accessToken.getExpiry();
    }

    public AccessToken getAccessToken() {
        Account account = getAccount();
        if (account == null) {
            return AccessToken.EMPTY;
        }
        String token = accountManager.peekAuthToken(account, authTokenType);
        long tokenExpiry = Long.parseLong(accountManager.getUserData(account, KEY_TOKEN_EXPIRY));
        return new AccessToken(token, tokenExpiry);
    }

    public void addAccount(Account account, AccessToken accessToken, RefreshToken refreshToken) {
        Bundle userData = new Bundle();
        userData.putString(WutsonAccountManager.KEY_TOKEN_EXPIRY, String.valueOf(accessToken.getExpiry()));
        accountManager.addAccountExplicitly(account, refreshToken.toString(), userData);
        accountManager.setAuthToken(account, account.type, accessToken.toString());
    }

    @Nullable
    public Account getAccount() {
        Account[] wutsonAccounts = accountManager.getAccountsByType(accountType);
        return wutsonAccounts.length == 0 ? null : wutsonAccounts[0];
    }

    public void signOut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            removeAccount();
        } else {
            removeAccountOldApis();
        }
    }

    @SuppressWarnings("deprecation") // only deprecated in API 22+
    private AccountManagerFuture<Boolean> removeAccountOldApis() {
        return accountManager.removeAccount(getAccount(), null, null);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private boolean removeAccount() {
        return accountManager.removeAccountExplicitly(getAccount());
    }

    public void invalidateAccessToken() {
        accountManager.invalidateAuthToken(accountType, getAccessToken().toString());
    }

}
