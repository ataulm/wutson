package com.ataulm.wutson.trackedshows;

import rx.Observer;

class TrackedShowsObserver implements Observer<TrackedShows> {

    private final Listener listener;
    private final TrackedShowsAdapter adapter;

    TrackedShowsObserver(Listener listener, TrackedShowsAdapter adapter) {
        this.listener = listener;
        this.adapter = adapter;
    }

    @Override
    public void onCompleted() {
        listener.onDataLoaded();
    }

    @Override
    public void onError(Throwable e) {
        listener.onErrorLoadingData(e);
    }

    @Override
    public void onNext(TrackedShows trackedShows) {
        adapter.update(trackedShows);
        adapter.notifyDataSetChanged();
    }

    public interface Listener {

        void onDataLoaded();
        void onErrorLoadingData(Throwable e);

    }

}
