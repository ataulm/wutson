package com.ataulm.wutson.repository;

import android.util.Log;

import com.ataulm.wutson.model.TmdbConfiguration;
import com.ataulm.wutson.repository.persistence.PersistentDataRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonConfiguration;
import com.google.gson.Gson;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class ConfigurationRepository {

    private final TmdbApi api;
    private final PersistentDataRepository persistentDataRepository;
    private final Gson gson;

    private final BehaviorSubject<TmdbConfiguration> subject;

    public ConfigurationRepository(TmdbApi api, PersistentDataRepository persistentDataRepository, Gson gson) {
        this.api = api;
        this.persistentDataRepository = persistentDataRepository;
        this.gson = gson;

        this.subject = BehaviorSubject.create();
    }

    public Observable<TmdbConfiguration> getConfiguration() {
        if (!subject.hasValue()) {
            refreshConfiguration();
        }
        return subject;
    }

    private void refreshConfiguration() {
        fetchJsonConfigurationFrom(persistentDataRepository)
                .flatMap(asGsonConfiguration(gson))
                .switchIfEmpty(api.getConfiguration().doOnNext(saveTo(persistentDataRepository, gson)))
                .flatMap(asTmdbConfiguration())
                .lift(Function.<TmdbConfiguration>swallowOnCompleteEvents())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    private static Func1<GsonConfiguration, Observable<TmdbConfiguration>> asTmdbConfiguration() {
        return new Func1<GsonConfiguration, Observable<TmdbConfiguration>>() {

            @Override
            public Observable<TmdbConfiguration> call(GsonConfiguration gsonConfiguration) {
                TmdbConfiguration tmdbConfiguration = new TmdbConfiguration(
                        gsonConfiguration.images.baseUrl,
                        gsonConfiguration.images.profileSizes,
                        gsonConfiguration.images.posterSizes,
                        gsonConfiguration.images.backdropSizes,
                        gsonConfiguration.images.stillSizes
                );
                return Observable.just(tmdbConfiguration);
            }

        };
    }

    private static Observable<String> fetchJsonConfigurationFrom(final PersistentDataRepository repository) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(repository.readJsonConfiguration());
                subscriber.onCompleted();
            }

        });
    }

    private static Func1<String, Observable<GsonConfiguration>> asGsonConfiguration(final Gson gson) {
        return new Func1<String, Observable<GsonConfiguration>>() {

            @Override
            public Observable<GsonConfiguration> call(final String json) {
                return Observable.create(new Observable.OnSubscribe<GsonConfiguration>() {

                    @Override
                    public void call(Subscriber<? super GsonConfiguration> subscriber) {
                        if (json.isEmpty()) {
                            Log.w("WHATWHAT", "Configuration json is empty");
                        } else {
                            GsonConfiguration gsonConfiguration = gson.fromJson(json, GsonConfiguration.class);
                            subscriber.onNext(gsonConfiguration);
                        }
                        subscriber.onCompleted();
                    }

                });
            }

        };
    }

    private static Action1<GsonConfiguration> saveTo(final PersistentDataRepository persistentDataRepository, final Gson gson) {
        return new Action1<GsonConfiguration>() {

            @Override
            public void call(GsonConfiguration gsonConfiguration) {
                String json = gson.toJson(gsonConfiguration, GsonConfiguration.class);
                persistentDataRepository.writeJsonConfiguration(json);
            }

        };
    }

}
