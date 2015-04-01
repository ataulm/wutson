package com.ataulm.wutson.discover;

import android.util.Log;

import com.ataulm.wutson.repository.persistence.PersistentDataRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonGenres;
import com.google.gson.Gson;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class GenresRepository {

    private final TmdbApi api;
    private final PersistentDataRepository persistentDataRepository;
    private final Gson gson;

    public GenresRepository(TmdbApi api, PersistentDataRepository persistentDataRepository, Gson gson) {
        this.api = api;
        this.persistentDataRepository = persistentDataRepository;
        this.gson = gson;
    }

    Observable<GsonGenres> getGenres() {
        return fetchJsonGenresFrom(persistentDataRepository)
                .flatMap(asGsonGenres(gson))
                .switchIfEmpty(api.getGenres().doOnNext(saveTo(persistentDataRepository, gson)))
                .subscribeOn(Schedulers.io());
    }

    private static Observable<String> fetchJsonGenresFrom(final PersistentDataRepository repository) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(repository.readJsonGenres());
                subscriber.onCompleted();
            }

        });
    }

    private static Func1<String, Observable<GsonGenres>> asGsonGenres(final Gson gson) {
        return new Func1<String, Observable<GsonGenres>>() {

            @Override
            public Observable<GsonGenres> call(final String json) {
                return Observable.create(new Observable.OnSubscribe<GsonGenres>() {

                    @Override
                    public void call(Subscriber<? super GsonGenres> subscriber) {
                        if (json.isEmpty()) {
                            Log.w("WHATWHAT", "Genres json is empty");
                        } else {
                            GsonGenres gsonGenres = gson.fromJson(json, GsonGenres.class);
                            subscriber.onNext(gsonGenres);
                        }
                        subscriber.onCompleted();
                    }

                });
            }

        };
    }

    private static Action1<GsonGenres> saveTo(final PersistentDataRepository persistentDataRepository, final Gson gson) {
        return new Action1<GsonGenres>() {

            @Override
            public void call(GsonGenres gsonGenres) {
                String json = gson.toJson(gsonGenres, GsonGenres.class);
                persistentDataRepository.writeJsonGenres(json);
            }

        };
    }

}
