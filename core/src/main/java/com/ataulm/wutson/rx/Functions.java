package com.ataulm.wutson.rx;

import com.ataulm.wutson.DeveloperError;

import rx.Observable;
import rx.functions.Func1;

public final class Functions {

    private Functions() {
        throw DeveloperError.nonInstantiableClass();
    }

    public static <T> Func1<Iterable<T>, Observable<T>> emitEachElement() {
        return new Func1<Iterable<T>, Observable<T>>() {

            @Override
            public Observable<T> call(Iterable<T> iterable) {
                return Observable.from(iterable);
            }

        };
    }

}
