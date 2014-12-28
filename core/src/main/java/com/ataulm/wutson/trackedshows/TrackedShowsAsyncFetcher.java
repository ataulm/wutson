package com.ataulm.wutson.trackedshows;

import com.ataulm.wutson.AsyncFetcher;
import com.ataulm.wutson.Fetcher;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class TrackedShowsAsyncFetcher implements AsyncFetcher<TrackedShows> {

    private final Fetcher<TrackedShows> fetcher;

    public static TrackedShowsAsyncFetcher newInstance() {
        return new TrackedShowsAsyncFetcher(new TrackedShowsFetcher());
    }

    TrackedShowsAsyncFetcher(Fetcher<TrackedShows> fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public Observable<TrackedShows> fetch() {
        return Observable.create(new Observable.OnSubscribe<TrackedShows>() {

            @Override
            public void call(Subscriber<? super TrackedShows> subscriber) {
                TrackedShows shows = fetcher.fetch();
                subscriber.onNext(shows);
                subscriber.onCompleted();
            }

        }).subscribeOn(Schedulers.io());
    }

}
