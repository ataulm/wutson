package com.ataulm.wutson.showdetails;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewPager;

import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.R;
import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.navigation.WutsonActivity;
import com.ataulm.wutson.rx.LoggingObserver;
import com.ataulm.wutson.shows.Show;
import com.ataulm.wutson.shows.ShowId;
import com.novoda.landingstrip.LandingStrip;

import java.net.URI;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ataulm.wutson.jabber.Jabber.dataRepository;

public class ShowDetailsActivity extends WutsonActivity implements OnClickSeasonListener {

    public static final String EXTRA_SHOW_TITLE = BuildConfig.APPLICATION_ID + ".show_title";
    public static final String EXTRA_SHOW_BACKDROP = BuildConfig.APPLICATION_ID + ".show_backdrop";
    public static final String EXTRA_SHOW_ACCENT_COLOR = BuildConfig.APPLICATION_ID + ".show_accent_color";

    private Subscription showDetailsSubscription;
    private ShowDetailsPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        ViewPager viewPager = (ViewPager) findViewById(R.id.show_details_pager_show);
        viewPager.setAdapter(adapter = new ShowDetailsPagerAdapter(getResources(), this, getLayoutInflater(), getShowBackdropUri()));
        ((LandingStrip) findViewById(R.id.tab_strip)).attach(viewPager);
    }

    private URI getShowBackdropUri() {
        return URI.create(getExtras().getString(EXTRA_SHOW_BACKDROP, ""));
    }

    private Bundle getExtras() {
        return getIntent().getExtras() != null ? getIntent().getExtras() : Bundle.EMPTY;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        applyTitleFromIntentExtras();
        applyColorFilterToAppBarIcons();
        getAppBarWidget().setBackgroundColor(getAccentColor());

        showDetailsSubscription = dataRepository().getShow(getShowId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ShowObserver());
    }

    private void applyTitleFromIntentExtras() {
        String showTitle = getShowTitle();
        setTitle(showTitle);
    }

    private String getShowTitle() {
        return getExtras().getString(EXTRA_SHOW_TITLE, getString(R.string.show_details_label));
    }

    private void applyColorFilterToAppBarIcons() {
        Drawable navigationIcon = getToolbar().getNavigationIcon();
        if (navigationIcon != null) {
            navigationIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
    }

    @ColorInt
    private int getAccentColor() {
        int fallbackColor = getResources().getColor(R.color.show_details_app_bar_background);
        return getExtras().getInt(EXTRA_SHOW_ACCENT_COLOR, fallbackColor);
    }

    private ShowId getShowId() {
        return new ShowId(getIntent().getData().getLastPathSegment());
    }

    @Override
    protected void onDestroy() {
        if (!showDetailsSubscription.isUnsubscribed()) {
            showDetailsSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(Show.SeasonSummary seasonSummary) {
        navigate().toSeason(getShowId(), getShowTitle(), seasonSummary.getSeasonNumber(), getAccentColor());
    }

    private class ShowObserver extends LoggingObserver<Show> {

        private ShowObserver() {
            super(Jabber.log());
        }

        @Override
        public void onNext(Show show) {
            super.onNext(show);
            adapter.update(show);
        }

    }

}
