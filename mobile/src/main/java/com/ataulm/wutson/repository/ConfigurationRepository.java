package com.ataulm.wutson.repository;

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
    private final BehaviorSubject<GsonConfiguration> subject;
    private final Gson gson;

    ConfigurationRepository(TmdbApi api, PersistentDataRepository persistentDataRepository) {
        this.api = api;
        this.persistentDataRepository = persistentDataRepository;
        this.subject = BehaviorSubject.create();

        this.gson = new Gson();
    }

    public Observable<GsonConfiguration> getConfiguration() {
        if (!subject.hasValue()) {
            refreshConfiguration();
        }
        return subject;
    }

    private void refreshConfiguration() {
        fetchJsonConfigurationFrom(persistentDataRepository)
                .flatMap(asGsonConfiguration(gson))
                .switchIfEmpty(api.getConfiguration().doOnNext(saveTo(persistentDataRepository, gson)))
                .lift(Function.<GsonConfiguration>swallowOnCompleteEvents())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
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
                        if (!json.isEmpty()) {
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
