package com.ataulm.wutson.repository;

import com.ataulm.wutson.repository.persistence.PersistentDataRepository;

import rx.Observable;
import rx.functions.Action1;
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

    Observable<Boolean> toggleTrackingShowWithId(String showId) {
        return getTrackedStatusOfShowWith(showId)
                .doOnNext(toggleTrackingShowWithId(showId, persistentDataRepository))
                .flatMap(new Func1<Boolean, Observable<Boolean>>() {

                    @Override
                    public Observable<Boolean> call(Boolean trackedStatusBeforeToggle) {
                        return Observable.just(!trackedStatusBeforeToggle);
                    }

                })
                .subscribeOn(Schedulers.io());
    }

    private static Action1<Boolean> toggleTrackingShowWithId(final String tmdbShowId, final PersistentDataRepository repository) {
        return new Action1<Boolean>() {

            @Override
            public void call(Boolean isTracked) {
                if (isTracked) {
                    repository.deleteFromTrackedShows(tmdbShowId);
                } else {
                    repository.addToTrackedShows(tmdbShowId);
                }
            }

        };
    }

}
