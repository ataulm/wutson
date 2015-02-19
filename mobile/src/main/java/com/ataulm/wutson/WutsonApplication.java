package com.ataulm.wutson;

import android.app.Application;

public class WutsonApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Jabber.init(this);
    }

}
