package com.ataulm.wutson.repository;

import com.ataulm.wutson.repository.persistence.PersistentDataRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonConfiguration;
import com.google.gson.Gson;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class ConfigurationRepository {

    private final TmdbApi api;
    private final PersistentDataRepository persistentDataRepository;
    private final BehaviorSubject<GsonConfiguration> subject;

    ConfigurationRepository(TmdbApi api, PersistentDataRepository persistentDataRepository) {
        this.api = api;
        this.persistentDataRepository = persistentDataRepository;
        this.subject = BehaviorSubject.create();
    }

    public Observable<GsonConfiguration> getConfiguration() {
        if (!subject.hasValue()) {
            refreshConfiguration();
        }
        return subject;
    }

    private void refreshConfiguration() {
        fetchGsonConfigurationFrom(persistentDataRepository)
                .switchIfEmpty(api.getConfiguration().doOnNext(saveTo(persistentDataRepository)))
                .lift(Function.<GsonConfiguration>swallowOnCompleteEvents())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    private static Observable<GsonConfiguration> fetchGsonConfigurationFrom(final PersistentDataRepository repository) {
        return Observable.create(new Observable.OnSubscribe<GsonConfiguration>() {

            @Override
            public void call(Subscriber<? super GsonConfiguration> subscriber) {
                String json = repository.readJsonConfiguration();
                if (!json.isEmpty()) {
                    // TODO: this new Gson() doesn't belong here
                    GsonConfiguration gsonConfiguration = new Gson().fromJson(json, GsonConfiguration.class);
                    subscriber.onNext(gsonConfiguration);
                }
                subscriber.onCompleted();
            }

        });
    }

    private static Action1<GsonConfiguration> saveTo(final PersistentDataRepository persistentDataRepository) {
        return new Action1<GsonConfiguration>() {

            @Override
            public void call(GsonConfiguration gsonConfiguration) {
                // TODO: this new Gson() doesn't belong here
                String json = new Gson().toJson(gsonConfiguration, GsonConfiguration.class);
                persistentDataRepository.writeJsonConfiguration(json);
            }

        };
    }

}
