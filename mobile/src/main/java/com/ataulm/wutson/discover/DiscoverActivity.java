package com.ataulm.wutson.discover;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.navigation.NavigationDrawerItem;
import com.ataulm.wutson.navigation.WutsonTopLevelActivity;
import com.ataulm.wutson.rx.LoggingObserver;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DiscoverActivity extends WutsonTopLevelActivity implements OnShowClickListener {

    private Subscription discoverShowsSubscription;
    private ViewPager viewPager;

    private DiscoverByGenrePagerAdapter adapter;

    @Override
    protected NavigationDrawerItem getNavigationDrawerItem() {
        return NavigationDrawerItem.DISCOVER_SHOWS;
    }

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
        navigate().toShow(show.getId(), show.getName());
    }

    private class Observer extends LoggingObserver<List<ShowsInGenre>> {

        @Override
        public void onNext(List<ShowsInGenre> showsSeparateByGenre) {
            super.onNext(showsSeparateByGenre);
            adapter.update(showsSeparateByGenre);
        }

    }

}
