package com.ataulm.wutson.rx;

import com.ataulm.wutson.DeveloperError;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public final class Function {

    private Function() {
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

    public static <T> Observable.Operator<T, T> swallowOnCompleteEvents() {
        return new Observable.Operator<T, T>() {

            @Override
            public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
                return new Subscriber<T>() {

                    @Override
                    public void onCompleted() {
                        // Swallow
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

        };
    }

}
