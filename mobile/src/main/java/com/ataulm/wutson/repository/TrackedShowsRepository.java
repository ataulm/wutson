package com.ataulm.wutson.repository;

import android.util.Log;

import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.repository.persistence.PersistentDataRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.tmdb.gson.GsonTvShow;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
        Observable<List<String>> listTrackedShowIdsObservable = Observable.create(new Observable.OnSubscribe<List<String>>() {

            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                subscriber.onNext(persistentDataRepository.getTmdbShowIdOfTrackedShows());
                subscriber.onCompleted();
            }

        });

        Observable<String> trackedShowIdObservable = listTrackedShowIdsObservable.flatMap(Function.<String>emitEachElement());
        trackedShowIdObservable
                .flatMap(fetchShowDetailsJsonFrom(persistentDataRepository))
                .flatMap(asGsonTvShow(gson))
                .flatMap(new Func1<GsonTvShow, Observable<ShowSummary>>() {
                    @Override
                    public Observable<ShowSummary> call(GsonTvShow gsonTvShow) {
                        // TODO: need poster and backdrop but need Configuration for that
                        return Observable.just(new ShowSummary(gsonTvShow.id, gsonTvShow.name, null, null));
                    }
                })
                .toList();

        return Observable.just(Collections.<ShowSummary>emptyList());
    }

    private static Func1<String, Observable<String>> fetchShowDetailsJsonFrom(final PersistentDataRepository repository) {
        return new Func1<String, Observable<String>>() {

            @Override
            public Observable<String> call(String tmdbShowId) {
                return Observable.just(repository.readJsonShowDetails(tmdbShowId));
            }

        };
    }

    private static Func1<String, Observable<GsonTvShow>> asGsonTvShow(final Gson gson) {
        return new Func1<String, Observable<GsonTvShow>>() {

            @Override
            public Observable<GsonTvShow> call(final String json) {
                return Observable.create(new Observable.OnSubscribe<GsonTvShow>() {

                    @Override
                    public void call(Subscriber<? super GsonTvShow> subscriber) {
                        if (json.isEmpty()) {
                            Log.w("WHATWHAT", "TvShow json is empty");
                        } else {
                            GsonTvShow gsonTvShow = gson.fromJson(json, GsonTvShow.class);
                            subscriber.onNext(gsonTvShow);
                        }
                        subscriber.onCompleted();
                    }

                });
            }

        };
    }

}
