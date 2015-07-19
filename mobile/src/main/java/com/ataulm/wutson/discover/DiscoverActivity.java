package com.ataulm.wutson.discover;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.ataulm.wutson.R;
import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.navigation.NavigationDrawerItem;
import com.ataulm.wutson.navigation.WutsonTopLevelActivity;
import com.ataulm.wutson.rx.LoggingObserver;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.shows.TrackedStatus;
import com.ataulm.wutson.shows.discover.DiscoverShows;
import com.novoda.landingstrip.LandingStrip;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ataulm.wutson.jabber.Jabber.dataRepository;

public class DiscoverActivity extends WutsonTopLevelActivity implements OnShowClickListener, OnClickShowSummaryListener {

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
            navigate().toMyShows();
        }
    }

    @Override
    protected void onDestroy() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(ShowSummary showSummary) {
        navigate().toShowDetails(showSummary.getId(), showSummary.getName(), showSummary.getBackdropUri().toString());
    }

    @Override
    public void onClickStopTracking(ShowSummary showSummary) {
        Jabber.dataRepository().setTrackedStatus(showSummary.getId(), TrackedStatus.NOT_TRACKED);
    }

    @Override
    public void onClickTrack(ShowSummary showSummary) {
        Jabber.dataRepository().setTrackedStatus(showSummary.getId(), TrackedStatus.TRACKED);
    }

    @Override
    public void onClickToggleTrackedStatus(ShowId showId) {
        dataRepository().toggleTrackedStatus(showId);
        // TODO: ensure changes like this kick off an onNext to cause the UI to update
    }

    private class Observer extends LoggingObserver<DiscoverShows> {

        private Observer() {
            super(Jabber.log());
        }

        @Override
        public void onNext(DiscoverShows discoverShows) {
            super.onNext(discoverShows);
            if (viewPager.getAdapter() == null) {
                adapter = new DiscoverShowsPagerAdapter(getLayoutInflater());
                adapter.update(discoverShows);
                viewPager.setAdapter(adapter);
                ((LandingStrip) findViewById(R.id.tab_strip)).attach(viewPager);
            } else {
                adapter.update(discoverShows);
            }
        }

    }

}
