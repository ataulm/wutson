package com.ataulm.wutson.repository;

import com.ataulm.wutson.model.Configuration;
import com.ataulm.wutson.model.TmdbApi;
import com.ataulm.wutson.rx.InfiniteOperator;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class ConfigurationRepository {

    private final TmdbApi api;
    private final BehaviorSubject<Configuration> subject;

    ConfigurationRepository(TmdbApi api) {
        this.api = api;
        this.subject = BehaviorSubject.create();
    }

    public Observable<Configuration> getConfiguration() {
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
