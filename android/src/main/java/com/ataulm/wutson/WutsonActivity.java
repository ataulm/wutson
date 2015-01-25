package com.ataulm.wutson;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.ataulm.wutson.repository.DataRepository;

public abstract class WutsonActivity extends ActionBarActivity {

    private WutsonApplication application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (WutsonApplication) getApplication();
    }

    protected DataRepository getDataRepository() {
        return application.getDataRepository();
    }

}
