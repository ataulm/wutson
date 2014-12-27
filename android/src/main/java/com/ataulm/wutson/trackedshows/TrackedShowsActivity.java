package com.ataulm.wutson.trackedshows;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ataulm.wutson.AsyncFetcher;
import com.ataulm.wutson.R;

import rx.Observable;
import rx.Subscription;

public class TrackedShowsActivity extends ActionBarActivity {

    private TrackedShowsAdapter trackedShowsAdapter;
    private Subscription trackedShowsSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracked_shows);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tracked_shows_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        trackedShowsAdapter = new TrackedShowsAdapter();
        recyclerView.setAdapter(trackedShowsAdapter);

        AsyncFetcher<TrackedShows> showsFetcher = new TrackedShowsFetcher();
        Observable<TrackedShows> trackedShowsObservable = showsFetcher.newFetchObservable();
        trackedShowsSubscription = trackedShowsObservable.subscribe(new Observer());
        // TODO: start loading-anims
    }

    @Override
    protected void onDestroy() {
        trackedShowsSubscription.unsubscribe();
        super.onDestroy();
    }

    // QUESTION: Is there any benefit to making this inner class static?
    private class Observer implements rx.Observer<TrackedShows> {

        @Override
        public void onCompleted() {
            // TODO: stop loading-anims
        }

        @Override
        public void onError(Throwable e) {
            // TODO: stop loading-anims
            // TODO: show error to user
        }

        @Override
        public void onNext(TrackedShows trackedShows) {
            trackedShowsAdapter.update(trackedShows);
            trackedShowsAdapter.notifyDataSetChanged();
        }

    }

}
