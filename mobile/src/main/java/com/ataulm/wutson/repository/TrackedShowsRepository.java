package com.ataulm.wutson.repository;

import com.ataulm.wutson.repository.persistence.PersistentDataRepository;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

final class TrackedShowsRepository {

    private final PersistentDataRepository persistentDataRepository;

    TrackedShowsRepository(PersistentDataRepository persistentDataRepository) {
        this.persistentDataRepository = persistentDataRepository;
    }

    Observable<Boolean> getTrackedStatusOfShowWith(String showId) {
        return Observable.just(persistentDataRepository.isShowTracked(showId));
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
            public Observable<Boolean> call(Boolean isTracked) {
                if (isTracked) {
                    repository.deleteFromTrackedShows(tmdbShowId);
                } else {
                    repository.addToTrackedShows(tmdbShowId);
                }
                return Observable.just(!isTracked);
            }

        };
    }

}
