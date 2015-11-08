package com.ataulm.wutson.auth;

import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AccountService extends Service {

    private WutsonAuthenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        this.authenticator = new WutsonAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (!AccountManager.ACTION_AUTHENTICATOR_INTENT.equals(intent.getAction())) {
            return null;
        }
        return authenticator.getIBinder();
    }

}
