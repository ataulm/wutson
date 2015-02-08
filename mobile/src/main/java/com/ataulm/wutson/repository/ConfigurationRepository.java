package com.ataulm.wutson.repository;

import com.ataulm.wutson.model.Configuration;
import com.ataulm.wutson.model.TmdbApi;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

class ConfigurationRepository {

    private final TmdbApi api;
    private final BehaviorSubject<Configuration> subject;

    ConfigurationRepository(TmdbApi api) {
        this.api = api;
        this.subject = BehaviorSubject.create();
    }

    Observable<Configuration> getConfiguration() {
        if (!subject.hasValue()) {
            refreshConfiguration();
        }
        return subject;
    }

    private void refreshConfiguration() {
        api.getConfiguration()
                .lift(new InfiniteOperator<Configuration>())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

}
