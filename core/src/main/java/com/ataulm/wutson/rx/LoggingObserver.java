package com.ataulm.wutson.rx;

import com.ataulm.wutson.Log;

import rx.Observer;

public class LoggingObserver<T> implements Observer<T> {

    private final Log log;

    public LoggingObserver(Log log) {
        this.log = log;
    }

    @Override
    public void onCompleted() {
        log.debug(getClass().getCanonicalName(), "onCompleted()");
    }

    @Override
    public void onError(Throwable e) {
        log.error(getClass().getCanonicalName(), "onError(): " + e.getMessage(), e);
    }

    @Override
    public void onNext(T t) {
        log.debug(getClass().getCanonicalName(), "onNext(t): " + t);
    }

}
