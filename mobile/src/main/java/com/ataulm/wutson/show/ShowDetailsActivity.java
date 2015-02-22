package com.ataulm.wutson.show;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.navigation.WutsonActivity;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShowDetailsActivity extends WutsonActivity {

    public static final String TMDB_SHOW_ID = "TMDB_SHOW_ID";

    private Subscription showDetailsSubscription;
    private ShowOverviewView showOverviewView;
    private PagerSlidingTabStrip tabStrip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        showOverviewView = (ShowOverviewView) findViewById(R.id.show_details_show);
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.show_details_tabs);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        customiseShowDetailsToolbar();
    }

    private void customiseShowDetailsToolbar() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Drawable navigationIcon = getToolbar().getNavigationIcon();
        if (navigationIcon != null) {
            navigationIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String showId = getIntent().getStringExtra(TMDB_SHOW_ID);
        showDetailsSubscription = Jabber.dataRepository().getShow(showId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer());
    }

    @Override
    protected void onPause() {
        if (!showDetailsSubscription.isUnsubscribed()) {
            showDetailsSubscription.unsubscribe();
        }
        super.onPause();
    }

    private class Observer implements rx.Observer<Show> {

        @Override
        public void onCompleted() {
            Log.d("THING", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            throw new Error(e);
        }

        @Override
        public void onNext(Show show) {
            Log.d("THING", "onNext: " + show.getName());
            showOverviewView.display(show);
            Palette.Swatch swatch = Jabber.swatches().get(show.getPosterUri());

            // TODO: title bg is back and text is black
//            setTitle(show.getName());
//            getSupportActionBar().setDisplayShowTitleEnabled(true);

            if (Jabber.swatches().hasSwatchFor(show.getPosterUri())) {
                getToolbar().setBackgroundColor(swatch.getRgb());
            }

            if (!show.getSeasons().isEmpty()) {
                tabStrip.setVisibility(View.VISIBLE);
                tabStrip.setBackgroundColor(swatch.getRgb());
            }

        }

    }

}
