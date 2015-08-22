package com.ataulm.wutson.seasons;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewPager;

import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.R;
import com.ataulm.wutson.episodes.Episode;
import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.navigation.WutsonActivity;
import com.ataulm.wutson.rx.LoggingObserver;
import com.ataulm.wutson.shows.ShowId;
import com.novoda.landingstrip.LandingStrip;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SeasonsActivity extends WutsonActivity implements OnClickEpisodeListener {

    public static final String EXTRA_SHOW_TITLE = BuildConfig.APPLICATION_ID + ".seasons_show_title";
    public static final String EXTRA_SHOW_ACCENT_COLOR = BuildConfig.APPLICATION_ID + ".show_accent_color";

    private static final String KEY_RESET_PAGE_POSITION = BuildConfig.APPLICATION_ID + ".KEY_RESET_PAGE_POSITION";
    private static final int URI_PATH_SEGMENT_SHOW_ID_INDEX = 1;

    private Subscription seasonSubscription;
    private ShowId showId;
    private int seasonNumber;
    private ViewPager pager;
    private LandingStrip tabs;

    private boolean shouldResetPagePosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasons);

        Uri data = getIntent().getData();
        showId = new ShowId(data.getPathSegments().get(URI_PATH_SEGMENT_SHOW_ID_INDEX));
        seasonNumber = Integer.parseInt(data.getLastPathSegment());

        tabs = (LandingStrip) findViewById(R.id.tab_strip);
        pager = (ViewPager) findViewById(R.id.seasons_pager);

        shouldResetPagePosition = savedInstanceState == null || savedInstanceState.getBoolean(KEY_RESET_PAGE_POSITION);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        customiseShowDetailsToolbar();

        seasonSubscription = Jabber.dataRepository().getSeasons(showId, getShowTitle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer());
    }

    private void customiseShowDetailsToolbar() {
        applyTitleFromIntentExtras();
        getAppBarWidget().setBackgroundColor(getAccentColor());
        Drawable navigationIcon = getToolbar().getNavigationIcon();
        if (navigationIcon != null) {
            navigationIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
    }

    private void applyTitleFromIntentExtras() {
        String showTitle = getShowTitle();
        setTitle(showTitle);
    }

    private String getShowTitle() {
        // TODO: seasons activity label
        return getExtras().getString(EXTRA_SHOW_TITLE, getString(R.string.app_name));
    }

    @ColorInt
    private int getAccentColor() {
        int fallbackColor = getResources().getColor(R.color.show_details_app_bar_background);
        return getExtras().getInt(EXTRA_SHOW_ACCENT_COLOR, fallbackColor);
    }

    private Bundle getExtras() {
        return getIntent().getExtras() != null ? getIntent().getExtras() : Bundle.EMPTY;
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
        navigate().toEpisodeDetails(showId, getShowTitle(), episode.getSeasonEpisodeNumber(), getAccentColor());
    }

    private class Observer extends LoggingObserver<Seasons> {

        private Observer() {
            super(Jabber.log());
        }

        @Override
        public void onNext(Seasons seasons) {
            SeasonsPagerAdapter adapter = new SeasonsPagerAdapter(seasons, SeasonsActivity.this, SeasonsActivity.this, getLayoutInflater(), getResources());
            pager.setAdapter(adapter);
            tabs.attach(pager);
            if (shouldResetPagePosition) {
                shouldResetPagePosition = false;
                pager.setCurrentItem(adapter.positionOfSeasonNumber(seasonNumber));
            }
        }

    }

}
