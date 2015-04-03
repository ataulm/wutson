package com.ataulm.wutson.repository;

import android.util.Log;

import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.model.TmdbConfiguration;
import com.ataulm.wutson.repository.persistence.PersistentDataRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.tmdb.gson.GsonTvShow;
import com.google.gson.Gson;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static com.ataulm.wutson.rx.Function.ignoreEmptyStrings;
import static com.ataulm.wutson.rx.Function.jsonTo;

public final class TrackedShowsRepository {

    private final PersistentDataRepository persistentDataRepository;
    private final ConfigurationRepository configurationRepo;
    private final Gson gson;

    public TrackedShowsRepository(PersistentDataRepository persistentDataRepository, ConfigurationRepository configurationRepo, Gson gson) {
        this.persistentDataRepository = persistentDataRepository;
        this.configurationRepo = configurationRepo;
        this.gson = gson;
    }

    Observable<Boolean> getTrackedStatusOfShowWith(final String showId) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(persistentDataRepository.isShowTracked(showId));
                subscriber.onCompleted();
            }

        });
    }

    /**
     * Toggles the tracked status, and returns the final status, after toggling.
     */
    Observable<Boolean> toggleTrackedStatusOfShowWith(String showId) {
        return getTrackedStatusOfShowWith(showId)
                .flatMap(toggleTrackedStatusOfShowWith(showId, persistentDataRepository))
                .subscribeOn(Schedulers.io());
    }

    private static Func1<Boolean, Observable<Boolean>> toggleTrackedStatusOfShowWith(final String tmdbShowId, final PersistentDataRepository repository) {
        return new Func1<Boolean, Observable<Boolean>>() {

            @Override
            public Observable<Boolean> call(Boolean isCurrentlyTracked) {
                toggleTrackedStatus(isCurrentlyTracked);
                return Observable.just(!isCurrentlyTracked);
            }

            private void toggleTrackedStatus(Boolean isCurrentlyTracked) {
                if (isCurrentlyTracked) {
                    repository.deleteFromTrackedShows(tmdbShowId);
                } else {
                    repository.addToTrackedShows(tmdbShowId);
                }
            }

        };
    }

    Observable<List<ShowSummary>> getMyShows() {
        Observable<TmdbConfiguration> repeatingConfigurationObservable = configurationRepo.getConfiguration().first().repeat();
        Observable<GsonTvShow> gsonTvShowsObservable = fetchListOfMyShowIdsFrom(persistentDataRepository)
                .flatMap(Function.<String>emitEachElement())
                .flatMap(fetchShowDetailsJsonFrom(persistentDataRepository))
                .filter(ignoreEmptyStrings())
                .map(jsonTo(GsonTvShow.class, gson));

        return Observable.zip(repeatingConfigurationObservable, gsonTvShowsObservable,asShowSummary())
                .toList();
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

    private static Func2<TmdbConfiguration, GsonTvShow, ShowSummary> asShowSummary() {
        return new Func2<TmdbConfiguration, GsonTvShow, ShowSummary>() {

            @Override
            public ShowSummary call(TmdbConfiguration tmdbConfiguration, GsonTvShow gsonTvShow) {
                return new ShowSummary(
                        gsonTvShow.id,
                        gsonTvShow.name,
                        tmdbConfiguration.completePoster(gsonTvShow.posterPath),
                        tmdbConfiguration.completeBackdrop(gsonTvShow.backdropPath)
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

}
