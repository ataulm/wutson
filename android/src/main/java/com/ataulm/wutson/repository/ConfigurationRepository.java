package com.ataulm.wutson.repository;

import com.ataulm.wutson.model.Configuration;
import com.ataulm.wutson.model.TmdbApi;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

class ConfigurationRepository {

    private final TmdbApi api;
    private final BehaviorSubject<Configuration> subject;

    private boolean initialised;

    ConfigurationRepository(TmdbApi api) {
        this.api = api;
        this.subject = BehaviorSubject.create();
    }

    Observable<Configuration> getConfiguration() {
        if (!initialised) {
            refreshConfiguration();
        }
        return subject;
    }

    private void refreshConfiguration() {
        api.getConfiguration()
                .doOnNext(markAsInitialised())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    private Action1<Configuration> markAsInitialised() {
        return new Action1<Configuration>() {

            @Override
            public void call(Configuration configuration) {
                initialised = true;
            }

        };
    }

}
