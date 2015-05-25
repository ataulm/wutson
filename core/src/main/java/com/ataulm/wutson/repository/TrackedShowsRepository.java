package com.ataulm.wutson.repository;

import com.ataulm.wutson.model.ShowSummaries;
import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.model.TrackedStatus;
import com.ataulm.wutson.repository.persistence.PersistentDataRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.tmdb.Configuration;
import com.ataulm.wutson.tmdb.gson.GsonTvShow;
import com.google.gson.Gson;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import static com.ataulm.wutson.rx.Function.ignoreEmptyStrings;
import static com.ataulm.wutson.rx.Function.jsonTo;

public final class TrackedShowsRepository {

    private final PersistentDataRepository persistentDataRepository;
    private final ConfigurationRepository configurationRepo;
    private final Gson gson;

    private final BehaviorSubject<ShowSummaries> subject;

    public TrackedShowsRepository(PersistentDataRepository persistentDataRepository, ConfigurationRepository configurationRepo, Gson gson) {
        this.persistentDataRepository = persistentDataRepository;
        this.configurationRepo = configurationRepo;
        this.gson = gson;

        this.subject = BehaviorSubject.create();
    }

    public Observable<ShowSummaries> getMyShows() {
        if (!subject.hasValue()) {
            refreshTrackedShows();
        }
        return subject;
    }

    private void refreshTrackedShows() {
        Observable<Configuration> repeatingConfigurationObservable = configurationRepo.getConfiguration().first().repeat();
        Observable<GsonTvShow> gsonTvShowsObservable = fetchListOfMyShowIdsFrom(persistentDataRepository)
                .flatMap(Function.<String>emitEachElement())
                .flatMap(fetchShowDetailsJsonFrom(persistentDataRepository))
                .filter(ignoreEmptyStrings())
                .map(jsonTo(GsonTvShow.class, gson));

        Observable.zip(repeatingConfigurationObservable, gsonTvShowsObservable, asShowSummary())
                .toList()
                .map(asShowSummaries())
                .lift(Function.<ShowSummaries>swallowOnCompleteEvents())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    public void toggleTrackedStatus(final String showId) {
        Observable.create(new Observable.OnSubscribe<Void>() {

            @Override
            public void call(Subscriber<? super Void> subscriber) {
                boolean currentlyTracked = persistentDataRepository.isShowTracked(showId);
                if (currentlyTracked) {
                    setTrackedStatus(showId, TrackedStatus.NOT_TRACKED);
                } else {
                    setTrackedStatus(showId, TrackedStatus.TRACKED);
                }
                refreshTrackedShows();
                subscriber.onCompleted();
            }

        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    void setTrackedStatus(final String showId, final TrackedStatus trackedStatus) {
        Observable.create(new Observable.OnSubscribe<Void>() {

            @Override
            public void call(Subscriber<? super Void> subscriber) {
                if (trackedStatus == TrackedStatus.TRACKED) {
                    persistentDataRepository.addToTrackedShows(showId);
                } else {
                    persistentDataRepository.deleteFromTrackedShows(showId);
                }
                refreshTrackedShows();
                subscriber.onCompleted();
            }

        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    private static Observable<List<String>> fetchListOfMyShowIdsFrom(final PersistentDataRepository persistentDataRepository) {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {

            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                subscriber.onNext(persistentDataRepository.getListOfTmdbShowIdsFromAllTrackedShows());
                subscriber.onCompleted();
            }

        });
    }

    private static Func2<Configuration, GsonTvShow, ShowSummary> asShowSummary() {
        return new Func2<Configuration, GsonTvShow, ShowSummary>() {

            @Override
            public ShowSummary call(Configuration configuration, GsonTvShow gsonTvShow) {
                return new ShowSummary(
                        gsonTvShow.id,
                        gsonTvShow.name,
                        configuration.completePoster(gsonTvShow.posterPath),
                        configuration.completeBackdrop(gsonTvShow.backdropPath)
                );
            }

        };
    }

    private static Func1<String, Observable<String>> fetchShowDetailsJsonFrom(final PersistentDataRepository repository) {
        return new Func1<String, Observable<String>>() {

            @Override
            public Observable<String> call(String tmdbShowId) {
                return Observable.just(repository.readJsonShowDetails(tmdbShowId));
            }

        };
    }

    private static Func1<List<ShowSummary>, ShowSummaries> asShowSummaries() {
        return new Func1<List<ShowSummary>, ShowSummaries>() {

            @Override
            public ShowSummaries call(List<ShowSummary> showSummaries) {
                return new ShowSummaries(showSummaries);
            }

        };
    }
}