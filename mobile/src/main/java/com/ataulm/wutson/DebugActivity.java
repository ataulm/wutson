package com.ataulm.wutson;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ataulm.wutson.auth.WutsonAccountManager;

import java.io.IOException;

public class DebugActivity extends AppCompatActivity {

    private TextView loggedInStatus;
    private WutsonAccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        loggedInStatus = ((TextView) findViewById(R.id.logged_in));
        accountManager = WutsonAccountManager.newInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLoggedInStatus();
    }

    private void updateLoggedInStatus() {
        Account account = accountManager.getAccount();
        if (account == null) {
            loggedInStatus.setText("not logged in");
            login(null);
        } else {
            loggedInStatus.setText("logged in as " + account.name);
        }
    }

    public void login(View view) {
        accountManager.startAddAccountProcess(this);
    }

}
