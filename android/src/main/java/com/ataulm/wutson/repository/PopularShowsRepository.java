package com.ataulm.wutson.repository;

import com.ataulm.wutson.model.PopularShows;
import com.ataulm.wutson.model.TmdbApi;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

class PopularShowsRepository {

    private final TmdbApi api;
    private final BehaviorSubject<PopularShows> subject;

    private boolean initialised;

    PopularShowsRepository(TmdbApi api) {
        this.api = api;
        this.subject = BehaviorSubject.create();
    }

    Observable<PopularShows> getPopularShows() {
        if (!initialised) {
            refreshPopularShows();
        }
        return subject;
    }

    private void refreshPopularShows() {
        api.getPopularShows()
                .doOnNext(markAsInitialised())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    private Action1<PopularShows> markAsInitialised() {
        return new Action1<PopularShows>() {

            @Override
            public void call(PopularShows shows) {
                initialised = true;
            }

        };
    }

}
