package com.ataulm.wutson.discover;

import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonGenres;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class GenresRepository {

    private final TmdbApi api;
    private final BehaviorSubject<GsonGenres> subject;

    GenresRepository(TmdbApi api) {
        this.api = api;
        this.subject = BehaviorSubject.create();
    }

    Observable<GsonGenres> getGenres() {
        if (!subject.hasValue()) {
            refreshGenres();
        }
        return subject;
    }

    private void refreshGenres() {
        api.getGenres()
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

}
