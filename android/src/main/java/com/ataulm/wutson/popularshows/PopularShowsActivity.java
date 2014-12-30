package com.ataulm.wutson.popularshows;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ataulm.wutson.AsyncFetcher;
import com.ataulm.wutson.R;

import rx.Observable;
import rx.Subscription;

public class PopularShowsActivity extends ActionBarActivity {

    private PopularShowsAdapter adapter;
    private Subscription popularShowsSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_shows);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.popular_shows_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.popular_shows_grid_items_horizontal)));
        adapter = new PopularShowsAdapter();
        recyclerView.setAdapter(adapter);

        AsyncFetcher<PopularShows> showsFetcher = PopularShowsAsyncFetcher.newInstance();
        Observable<PopularShows> popularShowsObservable = showsFetcher.fetch();
        popularShowsSubscription = popularShowsObservable.subscribe(new Observer());
        // TODO: start loading-indicator
    }

    @Override
    protected void onDestroy() {
        popularShowsSubscription.unsubscribe();
        super.onDestroy();
    }

    // QUESTION: Is there any benefit to making this inner class static?
    private class Observer implements rx.Observer<PopularShows> {

        @Override
        public void onCompleted() {
            // TODO: stop loading-indicator
        }

        @Override
        public void onError(Throwable e) {
            // TODO: stop loading-indicator
            // TODO: show error to user
        }

        @Override
        public void onNext(PopularShows popularShows) {
            adapter.update(popularShows);
        }

    }

}
