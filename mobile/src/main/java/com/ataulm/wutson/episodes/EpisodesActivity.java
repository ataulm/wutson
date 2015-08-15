package com.ataulm.wutson.episodes;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.R;
import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.navigation.WutsonActivity;
import com.ataulm.wutson.rx.LoggingObserver;
import com.ataulm.wutson.seasons.Season;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EpisodesActivity extends WutsonActivity {

    private static final String KEY_RESET_PAGE_POSITION = BuildConfig.APPLICATION_ID + ".KEY_RESET_PAGE_POSITION";

    private Subscription episodesSubscription;
    private ViewPager pager;
    private EpisodesPagerAdapter adapter;

    private boolean shouldResetPagePosition;
    private EpisodeActivityExtras episodeActivityExtras;
    private EpisodesUri episodesUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);

        episodesUri = EpisodesUri.from(getIntent().getData());
        episodeActivityExtras = EpisodeActivityExtras.from(getIntent(), getResources());

        pager = (ViewPager) findViewById(R.id.episodes_pager);
        pager.setAdapter(adapter = new EpisodesPagerAdapter(getLayoutInflater(), episodeActivityExtras.getAccentColor()));

        shouldResetPagePosition = savedInstanceState == null || savedInstanceState.getBoolean(KEY_RESET_PAGE_POSITION);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        updateTitle();
        Drawable navigationIcon = getToolbar().getNavigationIcon();
        if (navigationIcon != null) {
            navigationIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }

        episodesSubscription = Jabber.dataRepository().getSeason(episodesUri.getShowId(), episodesUri.getSeasonNumber(), episodeActivityExtras.getTitle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer());
    }

    private void updateTitle() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setTitle(episodeActivityExtras.getTitle() + " Season " + episodesUri.getSeasonNumber());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_RESET_PAGE_POSITION, shouldResetPagePosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (!episodesSubscription.isUnsubscribed()) {
            episodesSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    private class Observer extends LoggingObserver<Season> {

        private Observer() {
            super(Jabber.log());
        }

        @Override
        public void onNext(Season season) {
            super.onNext(season);
            adapter.update(season);
            if (shouldResetPagePosition) {
                shouldResetPagePosition = false;
                pager.setCurrentItem(adapter.positionOfEpisodeNumber(episodesUri.getSeasonEpisodeNumber()), false);
            }
        }

    }

}
