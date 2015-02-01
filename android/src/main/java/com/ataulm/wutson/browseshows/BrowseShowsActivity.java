package com.ataulm.wutson.browseshows;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ataulm.wutson.R;
import com.ataulm.wutson.WutsonTopLevelActivity;
import com.ataulm.wutson.settings.SettingsActivity;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BrowseShowsActivity extends WutsonTopLevelActivity {

    private Subscription browseShowsSubscription;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_shows);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.browse_shows_viewpager);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browse_shows, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return super.onOptionsItemSelected(item);
            case R.id.browse_shows_menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                throw new IllegalArgumentException("Unknown menu item: " + item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        browseShowsSubscription = getDataRepository().getShowsSeparatedByGenre()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BrowseShowsObserver());
    }

    @Override
    protected void onPause() {
        if (!browseShowsSubscription.isUnsubscribed()) {
            browseShowsSubscription.unsubscribe();
        }
        super.onPause();
    }

    private class BrowseShowsObserver implements rx.Observer<List<ShowsInGenre>> {

        @Override
        public void onCompleted() {
            Log.d("THING", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("THING", "Couldn't load data for BrowseShows", e);
        }

        @Override
        public void onNext(List<ShowsInGenre> showsSeparateByGenre) {
            Log.d("THING", "onNext");
            for (ShowsInGenre shows : showsSeparateByGenre) {
                Log.d("THING", "::: " + shows.getGenre() + " :::");
                for (Show show : shows) {
                    Log.d("THING", show.getName());
                }
            }
            viewPager.setAdapter(new BrowseShowsPagerAdapter(getLayoutInflater(), showsSeparateByGenre));
        }

    }

}
