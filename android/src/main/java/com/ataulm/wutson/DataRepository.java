package com.ataulm.wutson;

import com.ataulm.wutson.model.Configuration;
import com.ataulm.wutson.model.PopularShows;
import com.ataulm.wutson.model.TmdbApi;
import com.ataulm.wutson.model.TvShow;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class DataRepository {

    private final TmdbApi api;
    private final BehaviorSubject<PopularShows> popularShowsSubject;

    private boolean popularShowsInitialised;

    public DataRepository(TmdbApi api) {
        this.api = api;
        this.popularShowsSubject = BehaviorSubject.create();
    }

    public Observable<PopularShows> getPopularShows() {
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

    private Action1<PopularShows> markAsInitialised() {
        return new Action1<PopularShows>() {

            @Override
            public void call(PopularShows shows) {
                popularShowsInitialised = true;
            }

        };
    }

    public Observable<TvShow> getTvShow(String id) {
        return api.getTvShow(id);
    }

    public Observable<Configuration> getConfiguration() {
        return api.getConfiguration();
    }

}
