package com.ataulm.wutson.seasons;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.episodes.Episode;
import com.ataulm.wutson.navigation.WutsonActivity;
import com.ataulm.wutson.rx.LoggingObserver;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SeasonsActivity extends WutsonActivity implements OnClickEpisodeListener {

    private static final String KEY_RESET_PAGE_POSITION = BuildConfig.APPLICATION_ID + ".KEY_RESET_PAGE_POSITION";
    private static final int URI_PATH_SEGMENT_SHOW_ID_INDEX = 1;

    private Subscription seasonSubscription;
    private String showId;
    private int seasonNumber;
    private ViewPager pager;
    private PagerSlidingTabStrip tabs;

    private boolean shouldResetPagePosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasons);

        Uri data = getIntent().getData();
        showId = data.getPathSegments().get(URI_PATH_SEGMENT_SHOW_ID_INDEX);
        seasonNumber = Integer.parseInt(data.getLastPathSegment());

        tabs = (PagerSlidingTabStrip) findViewById(R.id.seasons_tabs);
        pager = (ViewPager) findViewById(R.id.seasons_pager);

        if (savedInstanceState == null) {
            shouldResetPagePosition = true;
        } else if (savedInstanceState.containsKey(KEY_RESET_PAGE_POSITION)) {
            shouldResetPagePosition = savedInstanceState.getBoolean(KEY_RESET_PAGE_POSITION);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        customiseShowDetailsToolbar();

        seasonSubscription = Jabber.dataRepository().getSeasons(showId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer());
    }

    private void customiseShowDetailsToolbar() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Drawable navigationIcon = getToolbar().getNavigationIcon();
        if (navigationIcon != null) {
            navigationIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_RESET_PAGE_POSITION, shouldResetPagePosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (!seasonSubscription.isUnsubscribed()) {
            seasonSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(Episode episode) {
        navigate().toEpisodeDetails(showId, episode.getSeasonNumber(), episode.getEpisodeNumber());
    }

    private class Observer extends LoggingObserver<Seasons> {

        @Override
        public void onNext(Seasons seasons) {
            pager.setAdapter(new SeasonsPagerAdapter(seasons, getLayoutInflater(), getResources()));
            tabs.setViewPager(pager);
            if (shouldResetPagePosition) {
                shouldResetPagePosition = false;
                pager.setCurrentItem(seasonNumber);
            }

            setTitle(seasons.getShowName());
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

    }

}
