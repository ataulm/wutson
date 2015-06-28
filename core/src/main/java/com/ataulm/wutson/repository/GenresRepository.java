package com.ataulm.wutson.repository;

import com.ataulm.wutson.repository.persistence.LocalDataRepository;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonGenres;
import com.google.gson.Gson;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.ataulm.wutson.rx.Function.jsonTo;
import static com.ataulm.wutson.rx.Function.ignoreEmptyStrings;

public class GenresRepository {

    private final TmdbApi api;
    private final LocalDataRepository localDataRepository;
    private final Gson gson;

    public GenresRepository(TmdbApi api, LocalDataRepository localDataRepository, Gson gson) {
        this.api = api;
        this.localDataRepository = localDataRepository;
        this.gson = gson;
    }

    Observable<GsonGenres> getGenres() {
        return fetchJsonGenresFrom(localDataRepository)
                .filter(ignoreEmptyStrings())
                .map(jsonTo(GsonGenres.class, gson))
                .switchIfEmpty(api.getGenres().doOnNext(saveTo(localDataRepository, gson)))
                .subscribeOn(Schedulers.io());
    }

    private static Observable<String> fetchJsonGenresFrom(final LocalDataRepository repository) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(repository.readJsonGenres());
                subscriber.onCompleted();
            }

        });
    }

    private static Action1<GsonGenres> saveTo(final LocalDataRepository localDataRepository, final Gson gson) {
        return new Action1<GsonGenres>() {

            @Override
            public void call(GsonGenres gsonGenres) {
                String json = gson.toJson(gsonGenres, GsonGenres.class);
                localDataRepository.writeJsonGenres(json);
            }

        };
    }

}
