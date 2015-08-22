package com.ataulm.wutson.discover;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewPager;

import com.ataulm.wutson.R;
import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.navigation.NavigationDrawerItem;
import com.ataulm.wutson.navigation.WutsonTopLevelActivity;
import com.ataulm.wutson.rx.LoggingObserver;
import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.shows.discover.DiscoverShows;
import com.novoda.landingstrip.LandingStrip;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DiscoverActivity extends WutsonTopLevelActivity {

    private Subscription subscription;

    private ViewPager viewPager;
    private DiscoverShowsPagerAdapter adapter;

    @Override
    protected NavigationDrawerItem getNavigationDrawerItem() {
        return NavigationDrawerItem.DISCOVER_SHOWS;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.discover_label);

        setContentView(R.layout.activity_discover);
        viewPager = (ViewPager) findViewById(R.id.discover_pager_genres);

        subscription = Jabber.discoverShowsRepository().getDiscoverShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer());
    }

    @Override
    public void onBackPressed() {
        if (closeNavigationDrawer()) {
            return;
        }
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    private class ClickListener implements DiscoverShowSummaryInteractionListener {

        @Override
        public void onClick(ShowSummary showSummary, @ColorInt int accentColor) {
            navigate().toShowDetails(showSummary.getId(), showSummary.getName(), showSummary.getBackdropUri().toString(), accentColor);
        }

    }

    private class Observer extends LoggingObserver<DiscoverShows> {

        private Observer() {
            super(Jabber.log());
        }

        @Override
        public void onNext(DiscoverShows discoverShows) {
            super.onNext(discoverShows);
            if (viewPager.getAdapter() == null) {
                adapter = DiscoverShowsPagerAdapter.newInstance(DiscoverActivity.this, new ClickListener(), DiscoverActivity.this);
                adapter.update(discoverShows);
                viewPager.setAdapter(adapter);
                ((LandingStrip) findViewById(R.id.tab_strip)).attach(viewPager);
            } else {
                adapter.update(discoverShows);
            }
        }

    }

}
