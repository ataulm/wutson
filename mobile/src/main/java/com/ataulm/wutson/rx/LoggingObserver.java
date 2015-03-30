package com.ataulm.wutson.rx;

import android.util.Log;

import rx.Observer;

public class LoggingObserver<T> implements Observer<T> {

    @Override
    public void onCompleted() {
        Log.d(getClass().getCanonicalName(), "onCompleted()");
    }

    @Override
    public void onError(Throwable e) {
        Log.e(getClass().getCanonicalName(), "onError(): " + e.getMessage());
    }

    @Override
    public void onNext(T t) {
        Log.d(getClass().getCanonicalName(), "onNext(t): " + t);
    }

}
