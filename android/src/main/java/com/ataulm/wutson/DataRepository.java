package com.ataulm.wutson;

import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.TmdbPopularShows;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class DataRepository {

    private final TmdbApi api;
    private final BehaviorSubject<TmdbPopularShows> popularShowsSubject;

    private boolean popularShowsInitialised;

    public DataRepository(TmdbApi api) {
        this.api = api;
        this.popularShowsSubject = BehaviorSubject.create();
    }

    public Observable<TmdbPopularShows> getPopularShows() {
        if (!popularShowsInitialised) {
            refreshPopularShows();
        }
        return popularShowsSubject;
    }

    private void refreshPopularShows() {
        api.getPopularShows()
                .doOnNext(markAsInitialised())
                .subscribeOn(Schedulers.io())
                .subscribe(popularShowsSubject);
    }

    private Action1<TmdbPopularShows> markAsInitialised() {
        return new Action1<TmdbPopularShows>() {

            @Override
            public void call(TmdbPopularShows shows) {
                popularShowsInitialised = true;
            }

        };
    }

}
