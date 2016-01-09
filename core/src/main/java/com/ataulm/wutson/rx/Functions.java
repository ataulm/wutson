package com.ataulm.wutson.rx;

import com.ataulm.wutson.DeveloperError;
import com.google.gson.Gson;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public final class Functions {

    private Functions() {
        throw DeveloperError.nonInstantiableClass();
    }

    public static Func1<String, Boolean> ignoreEmptyStrings() {
        return new Func1<String, Boolean>() {

            @Override
            public Boolean call(String string) {
                return string != null && !string.trim().isEmpty();
            }

        };
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

    public static <T> Func1<String, T> jsonTo(final Class<T> gsonClass, final Gson gson) {
        return new Func1<String, T>() {

            @Override
            public T call(String json) {
                return gson.fromJson(json, gsonClass);
            }

        };
    }

}
