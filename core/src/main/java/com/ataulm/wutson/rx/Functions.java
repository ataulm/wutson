package com.ataulm.wutson.rx;

import rx.Observable;
import rx.functions.Func1;

public final class Functions {

    private Functions() {
        // static rx function class
    }

    public static <T> Func1<Iterable<T>, Observable<T>> iterate() {
        return new Func1<Iterable<T>, Observable<T>>() {

            @Override
            public Observable<T> call(Iterable<T> iterable) {
                return Observable.from(iterable);
            }

        };
    }

}
