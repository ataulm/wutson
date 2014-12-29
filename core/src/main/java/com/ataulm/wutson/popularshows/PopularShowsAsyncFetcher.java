package com.ataulm.wutson.popularshows;

import com.ataulm.wutson.AsyncFetcher;
import com.ataulm.wutson.Fetcher;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class PopularShowsAsyncFetcher implements AsyncFetcher<PopularShows> {

    private final Fetcher<PopularShows> fetcher;

    public static PopularShowsAsyncFetcher newInstance() {
        return new PopularShowsAsyncFetcher(new PopularShowsFetcher());
    }

    PopularShowsAsyncFetcher(Fetcher<PopularShows> fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public Observable<PopularShows> fetch() {
        return Observable.create(new Observable.OnSubscribe<PopularShows>() {

            @Override
            public void call(Subscriber<? super PopularShows> subscriber) {
                PopularShows shows = fetcher.fetch();
                subscriber.onNext(shows);
                subscriber.onCompleted();
            }

        }).subscribeOn(Schedulers.io());
    }

}
