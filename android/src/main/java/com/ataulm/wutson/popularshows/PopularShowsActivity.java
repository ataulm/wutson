package com.ataulm.wutson.popularshows;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ataulm.wutson.R;
import com.ataulm.wutson.WutsonTopLevelActivity;
import com.ataulm.wutson.model.DiscoverTvShows;
import com.ataulm.wutson.model.PopularShows;
import com.ataulm.wutson.settings.SettingsActivity;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PopularShowsActivity extends WutsonTopLevelActivity {

    private Subscription popularShowsSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_shows);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popular_shows, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return super.onOptionsItemSelected(item);
            case R.id.popular_shows_menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                throw new IllegalArgumentException("Unknown menu item: " + item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        popularShowsSubscription = getDataRepository().getDiscoverTvShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DiscoverTvShowsObserver());
    }

    @Override
    protected void onPause() {
        if (!popularShowsSubscription.isUnsubscribed()) {
            popularShowsSubscription.unsubscribe();
        }
        super.onPause();
    }

    private class Observer implements rx.Observer<PopularShows> {

        @Override
        public void onCompleted() {
            Log.d("THING", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            throw new Error(e);
        }

        @Override
        public void onNext(PopularShows popularShows) {
            Log.d("THING", "onNext");
            for (PopularShows.Show popularShow : popularShows) {
                Log.d("THING", popularShow.toString());
            }
        }

    }

    private class DiscoverTvShowsObserver implements rx.Observer<List<DiscoverTvShows>> {

        @Override
        public void onCompleted() {
            Log.d("THING", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            throw new Error(e);
        }

        @Override
        public void onNext(List<DiscoverTvShows> discoverTvShowsList) {
            Log.d("THING", "onNext");
            for (DiscoverTvShows discoverTvShows : discoverTvShowsList) {
                Log.d("THING", "::: GENRE :::");
                for (DiscoverTvShows.Show show : discoverTvShows) {
                    Log.d("THING", show.toString());
                }
            }
        }

    }

}
