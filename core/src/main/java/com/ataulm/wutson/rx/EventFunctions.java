package com.ataulm.wutson.rx;

import com.ataulm.wutson.repository.event.Event;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

public class EventFunctions {

    public static <T> Func1<T, Event<T>> asIdleEventWithData() {
        return new Func1<T, Event<T>>() {

            @Override
            public Event<T> call(T t) {
                return Event.idle(t);
            }

        };
    }

    public static <T> Observable<Event<T>> sendIdleEventTo(final BehaviorSubject<Event<T>> subject) {
        return Observable.create(
                new Observable.OnSubscribe<Event<T>>() {

                    @Override
                    public void call(Subscriber<? super Event<T>> subscriber) {
                        Event<T> cachedEvent = subject.getValue();

                        if (cachedEvent != null && cachedEvent.getData().isPresent()) {
                            subject.onNext(Event.idle(cachedEvent.getData().get()));
                        } else {
                            subject.onNext(Event.<T>idle());
                        }
                    }

                }
        );
    }

    public static <T> Func1<Throwable, Event<T>> sendErrorEventTo(final BehaviorSubject<Event<T>> subject) {
        return new Func1<Throwable, Event<T>>() {

            @Override
            public Event<T> call(Throwable throwable) {
                Event<T> cachedEvent = subject.getValue();

                if (cachedEvent != null && cachedEvent.getData().isPresent()) {
                    return Event.error(cachedEvent.getData().get(), throwable);
                } else {
                    return Event.error(throwable);
                }
            }

        };
    }

    public static <T> Action0 sendLoadingEventTo(final BehaviorSubject<Event<T>> subject) {
        return new Action0() {

            @Override
            public void call() {
                final Event<T> cachedEvent = subject.getValue();
                if (cachedEvent != null && cachedEvent.getData().isPresent()) {
                    subject.onNext(Event.loading(cachedEvent.getData().get()));
                } else {
                    subject.onNext(Event.<T>loading());
                }
            }

        };
    }

}
