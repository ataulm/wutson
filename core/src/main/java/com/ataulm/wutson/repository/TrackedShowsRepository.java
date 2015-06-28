package com.ataulm.wutson.repository;

import com.ataulm.wutson.model.ShowId;
import com.ataulm.wutson.model.ShowSummaries;
import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.model.TrackedStatus;
import com.ataulm.wutson.repository.persistence.LocalDataRepository;
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

    private final LocalDataRepository localDataRepository;
    private final ConfigurationRepository configurationRepo;
    private final Gson gson;

    private final BehaviorSubject<ShowSummaries> subject;

    public TrackedShowsRepository(LocalDataRepository localDataRepository, ConfigurationRepository configurationRepo, Gson gson) {
        this.localDataRepository = localDataRepository;
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
        Observable<GsonTvShow> gsonTvShowsObservable = getTrackedShowIds()
                .flatMap(Function.<ShowId>emitEachElement())
                .flatMap(fetchShowDetailsJsonFrom(localDataRepository))
                .filter(ignoreEmptyStrings())
                .map(jsonTo(GsonTvShow.class, gson));

        Observable.zip(repeatingConfigurationObservable, gsonTvShowsObservable, asShowSummary())
                .toList()
                .map(asShowSummaries())
                .lift(Function.<ShowSummaries>swallowOnCompleteEvents())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    public Observable<List<ShowId>> getTrackedShowIds() {
        return fetchListOfMyShowIdsFrom(localDataRepository);
    }

    public void toggleTrackedStatus(final ShowId showId) {
        Observable.create(new Observable.OnSubscribe<Void>() {

            @Override
            public void call(Subscriber<? super Void> subscriber) {
                boolean currentlyTracked = localDataRepository.isShowTracked(showId);
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

    void setTrackedStatus(final ShowId showId, final TrackedStatus trackedStatus) {
        Observable.create(new Observable.OnSubscribe<Void>() {

            @Override
            public void call(Subscriber<? super Void> subscriber) {
                if (trackedStatus == TrackedStatus.TRACKED) {
                    localDataRepository.addToTrackedShows(showId);
                } else {
                    localDataRepository.deleteFromTrackedShows(showId);
                }
                refreshTrackedShows();
                subscriber.onCompleted();
            }

        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    private static Observable<List<ShowId>> fetchListOfMyShowIdsFrom(final LocalDataRepository localDataRepository) {
        return Observable.create(new Observable.OnSubscribe<List<ShowId>>() {

            @Override
            public void call(Subscriber<? super List<ShowId>> subscriber) {
                subscriber.onNext(localDataRepository.getListOfTmdbShowIdsFromAllTrackedShows());
                subscriber.onCompleted();
            }

        });
    }

    private static Func2<Configuration, GsonTvShow, ShowSummary> asShowSummary() {
        return new Func2<Configuration, GsonTvShow, ShowSummary>() {

            @Override
            public ShowSummary call(Configuration configuration, GsonTvShow gsonTvShow) {
                return new ShowSummary(
                        new ShowId(gsonTvShow.id),
                        gsonTvShow.name,
                        configuration.completePoster(gsonTvShow.posterPath),
                        configuration.completeBackdrop(gsonTvShow.backdropPath)
                );
            }

        };
    }

    private static Func1<ShowId, Observable<String>> fetchShowDetailsJsonFrom(final LocalDataRepository repository) {
        return new Func1<ShowId, Observable<String>>() {

            @Override
            public Observable<String> call(ShowId tmdbShowId) {
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
