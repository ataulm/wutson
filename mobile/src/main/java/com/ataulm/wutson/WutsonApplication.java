package com.ataulm.wutson;

import android.app.Application;
import com.crashlytics.android.Crashlytics;

public class WutsonApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Crashlytics.start(this);
        Jabber.init(this);
    }

}
