package com.ataulm.wutson;

import android.app.Application;

import com.ataulm.wutson.jabber.Jabber;
import com.crashlytics.android.Crashlytics;

public class WutsonApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Crashlytics.start(this);
        Jabber.init(this, BuildConfig.TMDB_API_KEY);
    }

}
