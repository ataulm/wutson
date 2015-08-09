package com.ataulm.wutson.showdetails;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.shows.Show;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.TrackedStatus;
import com.ataulm.wutson.navigation.WutsonActivity;
import com.ataulm.wutson.rx.LoggingObserver;
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

    private Subscription trackedStatusSubscription;
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
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return URI.create("");
        }
        return URI.create(extras.getString(EXTRA_SHOW_BACKDROP, ""));
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

    @ColorInt
    private int getAccentColor() {
        Bundle extras = getIntent().getExtras();
        return extras.getInt(EXTRA_SHOW_ACCENT_COLOR, getResources().getColor(R.color.show_details_app_bar_background));
    }

    private void applyTitleFromIntentExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        String showTitle = extras.getString(EXTRA_SHOW_TITLE, "");
        setTitle(showTitle);
    }

    private void applyColorFilterToAppBarIcons() {
        Drawable navigationIcon = getToolbar().getNavigationIcon();
        if (navigationIcon != null) {
            navigationIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_details, menu);

        MenuItem trackMenuItem = menu.findItem(R.id.show_details_menu_item_toggle_track);
        trackedStatusSubscription = dataRepository().getTrackedStatus(getShowId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TrackingShowObserver(trackMenuItem));

        return true;
    }

    private ShowId getShowId() {
        return new ShowId(getIntent().getData().getLastPathSegment());
    }

    @Override
    protected void onDestroy() {
        if (!showDetailsSubscription.isUnsubscribed()) {
            showDetailsSubscription.unsubscribe();
        }
        if (!trackedStatusSubscription.isUnsubscribed()) {
            trackedStatusSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(Show.SeasonSummary seasonSummary) {
        navigate().toSeason(getShowId(), seasonSummary.getSeasonNumber());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.show_details_menu_item_toggle_track) {
            dataRepository().toggleTrackedStatus(getShowId());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private class TrackingShowObserver extends LoggingObserver<TrackedStatus> {

        private final MenuItem item;

        TrackingShowObserver(MenuItem item) {
            super(Jabber.log());
            this.item = item;
        }

        @Override
        public void onNext(TrackedStatus trackedStatus) {
            super.onNext(trackedStatus);
            if (trackedStatus == TrackedStatus.TRACKED) {
                updateMenuItemToReflectThatShowIsBeingTracked();
            } else {
                updateMenuItemToReflectThatShowIsNotBeingTracked();
            }
        }

        private void updateMenuItemToReflectThatShowIsBeingTracked() {
            item.setIcon(R.drawable.ic_action_star_full);
            item.setTitle("Stop tracking show");
        }

        private void updateMenuItemToReflectThatShowIsNotBeingTracked() {
            item.setIcon(R.drawable.ic_action_star);
            item.setTitle("Start tracking show");
        }

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
