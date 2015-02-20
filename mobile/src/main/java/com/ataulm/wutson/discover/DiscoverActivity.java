package com.ataulm.wutson.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.navigation.WutsonTopLevelActivity;
import com.ataulm.wutson.show.ShowDetailsActivity;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DiscoverActivity extends WutsonTopLevelActivity implements OnShowClickListener {

    private Subscription discoverShowsSubscription;
    private ViewPager viewPager;

    private DiscoverByGenrePagerAdapter adapter;
    private PagerSlidingTabStrip tabs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.discover_label);

        setContentView(R.layout.activity_discover);

        viewPager = (ViewPager) findViewById(R.id.discover_viewpager);
        viewPager.setAdapter(adapter = new DiscoverByGenrePagerAdapter(getLayoutInflater(), this));

        ((PagerSlidingTabStrip) findViewById(R.id.tabs)).setViewPager(viewPager);
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
    protected void onResume() {
        super.onResume();
        discoverShowsSubscription = Jabber.dataRepository().getShowsSeparatedByGenre()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer());
    }

    @Override
    protected void onPause() {
        if (!discoverShowsSubscription.isUnsubscribed()) {
            discoverShowsSubscription.unsubscribe();
        }
        super.onPause();
    }

    @Override
    public void onClick(Show show) {
        startActivity(new Intent(this, ShowDetailsActivity.class).putExtra(ShowDetailsActivity.TMDB_SHOW_ID, show.getId()));
    }

    private class Observer implements rx.Observer<List<ShowsInGenre>> {

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
            adapter.update(showsSeparateByGenre);
        }

    }

}
