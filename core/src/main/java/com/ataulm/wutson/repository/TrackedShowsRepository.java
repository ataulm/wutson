package com.ataulm.wutson.repository;

import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.ShowSummaries;
import com.ataulm.wutson.shows.TrackedStatus;
import com.google.gson.Gson;

import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public final class TrackedShowsRepository {

    private final Gson gson;
    private final BehaviorSubject<ShowSummaries> subject;

    public TrackedShowsRepository(Gson gson) {
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
    }

    public Observable<List<ShowId>> getTrackedShowIds() {
        return Observable.empty();
    }

    public void toggleTrackedStatus(final ShowId showId) {
    }

    void setTrackedStatus(final ShowId showId, final TrackedStatus trackedStatus) {
    }

}
