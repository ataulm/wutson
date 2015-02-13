package com.ataulm.wutson.rx;

import rx.Observable;
import rx.Subscriber;

public class InfiniteOperator<T> implements Observable.Operator<T, T> {

    @Override
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>() {
            @Override
            public void onCompleted() {
                //Swallow
            }

            @Override
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override
            public void onNext(T edition) {
                subscriber.onNext(edition);
            }
        };
    }

}
