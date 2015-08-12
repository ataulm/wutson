package com.ataulm.wutson;

public class AndroidLog implements Log {

    @Override
    public void verbose(String tag, String message) {
        android.util.Log.v(tag, message);
    }

    @Override
    public void debug(String tag, String message) {
        android.util.Log.d(tag, message);
    }

    @Override
    public void error(String tag, String message) {
        android.util.Log.e(tag, message);
    }

    @Override
    public void error(String tag, String message, Throwable throwable) {
        android.util.Log.e(tag, message, throwable);
    }

}
