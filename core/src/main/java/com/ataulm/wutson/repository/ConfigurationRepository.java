package com.ataulm.wutson.repository;

import com.ataulm.wutson.core.model.TmdbConfiguration;
import com.ataulm.wutson.repository.persistence.PersistentDataRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.core.tmdb.TmdbApi;
import com.ataulm.wutson.core.tmdb.gson.GsonConfiguration;
import com.google.gson.Gson;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import static com.ataulm.wutson.rx.Function.ignoreEmptyStrings;
import static com.ataulm.wutson.rx.Function.jsonTo;

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
                .filter(ignoreEmptyStrings())
                .map(jsonTo(GsonConfiguration.class, gson))
                .switchIfEmpty(api.getConfiguration().doOnNext(saveTo(persistentDataRepository, gson)))
                .map(asTmdbConfiguration())
                .lift(Function.<TmdbConfiguration>swallowOnCompleteEvents())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    private static Func1<GsonConfiguration, TmdbConfiguration> asTmdbConfiguration() {
        return new Func1<GsonConfiguration, TmdbConfiguration>() {

            @Override
            public TmdbConfiguration call(GsonConfiguration gsonConfiguration) {
                return new TmdbConfiguration(
                        gsonConfiguration.images.baseUrl,
                        gsonConfiguration.images.profileSizes,
                        gsonConfiguration.images.posterSizes,
                        gsonConfiguration.images.backdropSizes,
                        gsonConfiguration.images.stillSizes
                );
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
