package com.ataulm.wutson.shows.discover;

import com.ataulm.wutson.repository.persistence.LocalDataRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonGenres;
import com.google.gson.Gson;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import static com.ataulm.wutson.rx.Function.ignoreEmptyStrings;
import static com.ataulm.wutson.rx.Function.jsonTo;

class GenresRepository {

    private final TmdbApi api;
    private final LocalDataRepository localDataRepository;
    private final Gson gson;

    private final BehaviorSubject<GsonGenres> subject;

    GenresRepository(TmdbApi api, LocalDataRepository localDataRepository, Gson gson) {
        this.api = api;
        this.localDataRepository = localDataRepository;
        this.gson = gson;

        this.subject = BehaviorSubject.create();
    }

    public Observable<GsonGenres> getGenres() {
        if (!subject.hasValue()) {
            Observable.concat(genresFromDisk(), genresFromNetwork())
                    .first()
                    .lift(Function.<GsonGenres>swallowOnCompleteEvents())
                    .subscribeOn(Schedulers.io())
                    .subscribe(subject);
        }
        return subject;
    }

    private Observable<GsonGenres> genresFromDisk() {
        return fetchJsonGenresFrom(localDataRepository)
                .filter(ignoreEmptyStrings())
                .map(jsonTo(GsonGenres.class, gson));
    }

    private Observable<GsonGenres> genresFromNetwork() {
        return api.getGenres()
                .doOnNext(saveTo(localDataRepository, gson));
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
