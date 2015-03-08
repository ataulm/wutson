package com.ataulm.wutson.discover;

import com.ataulm.wutson.tmdb.GsonGenres;
import com.ataulm.wutson.tmdb.TmdbApi;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class GenresRepository {

    private final TmdbApi api;
    private final BehaviorSubject<GsonGenres> subject;

    private boolean initialised;

    GenresRepository(TmdbApi api) {
        this.api = api;
        this.subject = BehaviorSubject.create();
    }

    Observable<GsonGenres> getGenres() {
        if (!initialised) {
            refreshGenres();
        }
        return subject;
    }

    private void refreshGenres() {
        api.getGenres()
                .doOnNext(markAsInitialised())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    private Action1<GsonGenres> markAsInitialised() {
        return new Action1<GsonGenres>() {

            @Override
            public void call(GsonGenres gsonGenres) {
                initialised = true;
            }

        };
    }

}
