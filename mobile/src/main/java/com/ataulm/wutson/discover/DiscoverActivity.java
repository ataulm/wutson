package com.ataulm.wutson.discover;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.navigation.NavigationDrawerItem;
import com.ataulm.wutson.navigation.WutsonTopLevelActivity;
import com.ataulm.wutson.rx.LoggingObserver;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DiscoverActivity extends WutsonTopLevelActivity implements OnShowClickListener {

    private static final String KEY_CURRENT_PAGE = BuildConfig.APPLICATION_ID + ".KEY_CURRENT_PAGE";

    private Subscription discoverShowsSubscription;
    private ViewPager viewPager;

    private GenresPagerAdapter adapter;
    private int pageToRestore;

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
        viewPager.setAdapter(adapter = new GenresPagerAdapter(getLayoutInflater(), this));

        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.discover_tabs_genres);
        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                DiscoverActivity.this.pageToRestore = position;
            }

        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pageToRestore = savedInstanceState.getInt(KEY_CURRENT_PAGE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        discoverShowsSubscription = Jabber.dataRepository().getDiscoverShowsList()
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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_CURRENT_PAGE, viewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        if (!discoverShowsSubscription.isUnsubscribed()) {
            discoverShowsSubscription.unsubscribe();
        }
        super.onPause();
    }

    @Override
    public void onClick(ShowSummary showSummary) {
        navigate().toShowDetails(showSummary.getId(), showSummary.getName(), showSummary.getBackdropUri().toString());
    }

    private class Observer extends LoggingObserver<List<ShowsInGenre>> {

        @Override
        public void onNext(List<ShowsInGenre> showsSeparateByGenre) {
            super.onNext(showsSeparateByGenre);
            adapter.update(showsSeparateByGenre);
            viewPager.setCurrentItem(pageToRestore);
        }

    }

}
