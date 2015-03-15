package com.ataulm.wutson.show;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.navigation.WutsonActivity;
import com.ataulm.wutson.rx.LoggingObserver;

import java.net.URI;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShowDetailsActivity extends WutsonActivity implements OnClickSeasonListener {

    public static final String EXTRA_SHOW_TITLE = BuildConfig.APPLICATION_ID + ".show_title";
    public static final String EXTRA_SHOW_BACKDROP = BuildConfig.APPLICATION_ID + ".show_backdrop";

    private Subscription showDetailsSubscription;
    private ShowPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        ViewPager viewPager = (ViewPager) findViewById(R.id.show_details_pager_show);
        viewPager.setAdapter(adapter = new ShowPagerAdapter(getResources(), this, getLayoutInflater(), getShowBackdropUri()));
        ((PagerSlidingTabStrip) findViewById(R.id.show_details_tabs_show)).setViewPager(viewPager);
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
    protected void onResume() {
        super.onResume();
        showDetailsSubscription = Jabber.dataRepository().getShow(getShowId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer());
    }

    private String getShowId() {
        return getIntent().getData().getLastPathSegment();
    }

    @Override
    protected void onPause() {
        if (!showDetailsSubscription.isUnsubscribed()) {
            showDetailsSubscription.unsubscribe();
        }
        super.onPause();
    }

    @Override
    public void onClick(Show.Season season) {
        navigate().toSeason(getShowId(), season.getSeasonNumber());
    }

    private class Observer extends LoggingObserver<Show> {

        @Override
        public void onNext(Show show) {
            super.onNext(show);
            adapter.update(show);
        }

    }

}
