package com.ataulm.wutson.repository;

import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.subjects.BehaviorSubject;

final class TrackedShowsRepository {

    private static final String KEY_TRACKED_SHOWS = "key_tracked_shows";

    private final SharedPreferences sharedPreferences;
    private final Set<String> idsOfTrackedShows;
    private final BehaviorSubject<Boolean> subject;

    static TrackedShowsRepository newInstance(SharedPreferences sharedPreferences) {
        Set<String> idsOfTrackedShows = sharedPreferences.getStringSet(KEY_TRACKED_SHOWS, new HashSet<String>());
        return new TrackedShowsRepository(idsOfTrackedShows, sharedPreferences);
    }

    private TrackedShowsRepository(Set<String> idsOfTrackedShows, SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.subject = BehaviorSubject.create();
        this.idsOfTrackedShows = idsOfTrackedShows;
    }

    Observable<Boolean> getTrackedStatusOfShowWith(String showId) {
        if (!subject.hasValue()) {
            subject.onNext(idsOfTrackedShows.contains(showId));
        }
        return subject;
    }

    void toggleTrackingShowWithId(String showId) {
        if (idsOfTrackedShows.contains(showId)) {
            stopTrackingShowWithId(showId);
        } else {
            startTrackingShowWithId(showId);
        }
    }

    private void startTrackingShowWithId(String showId) {
        if (idsOfTrackedShows.add(showId)) {
            resynchronisePrefs();
        }
        subject.onNext(true);
    }

    private void stopTrackingShowWithId(String showId) {
        if (idsOfTrackedShows.remove(showId)) {
            resynchronisePrefs();
        }
        subject.onNext(false);
    }

    private void resynchronisePrefs() {
        sharedPreferences.edit().putStringSet(KEY_TRACKED_SHOWS, idsOfTrackedShows).apply();
    }

}
