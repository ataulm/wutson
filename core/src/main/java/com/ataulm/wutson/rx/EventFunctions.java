package com.ataulm.wutson.rx;

import com.ataulm.wutson.repository.event.Event;

import rx.Observable;
import rx.functions.Func1;

public class EventFunctions {

    public static <T> Observable.Transformer<T, Event<T>> asEvents() {
        return new Observable.Transformer<T, Event<T>>() {

            @Override
            public Observable<Event<T>> call(Observable<T> data) {
                return data
                        .single()
                        .map(EventFunctions.<T>asIdleEventWithData())
                        .startWith(EventFunctions.<T>loadingEvent())
                        .onErrorReturn(EventFunctions.<T>errorEvent());
            }

        };
    }

    private static <T> Event<T> loadingEvent() {
        return Event.loading();
    }

    private static <T> Func1<Throwable, Event<T>> errorEvent() {
        return new Func1<Throwable, Event<T>>() {

            @Override
            public Event<T> call(Throwable throwable) {
                return Event.error(throwable);
            }

        };
    }

    private static <T> Func1<T, Event<T>> asIdleEventWithData() {
        return new Func1<T, Event<T>>() {

            @Override
            public Event<T> call(T t) {
                return Event.idle(t);

            }

        };
    }

}
