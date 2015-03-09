package com.ataulm.wutson.seasons;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;

import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.navigation.WutsonActivity;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SeasonsActivity extends WutsonActivity {

    private static final int URI_PATH_SEGMENT_SHOW_ID = 1;

    private Subscription seasonSubscription;
    private String showId;
    private int seasonNumber;
    private SeasonsView seasonsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasons);

        Uri data = getIntent().getData();
        showId = data.getPathSegments().get(URI_PATH_SEGMENT_SHOW_ID);
        seasonNumber = Integer.parseInt(data.getLastPathSegment());
        seasonsView = (SeasonsView) findViewById(R.id.seasons);
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
        seasonSubscription = Jabber.dataRepository().getSeasons(showId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer());
    }

    @Override
    protected void onPause() {
        if (!seasonSubscription.isUnsubscribed()) {
            seasonSubscription.unsubscribe();
        }
        super.onPause();
    }

    private class Observer implements rx.Observer<Seasons> {

        @Override
        public void onCompleted() {
            Log.d("THING", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("THING", "Couldn't load data for Seasons", e);
        }

        @Override
        public void onNext(Seasons seasons) {
            seasonsView.display(seasons);

            Palette.Swatch swatch = Jabber.swatches().get(showId);
            int rgb = swatch.getRgb();
            getToolbar().setBackgroundColor(rgb);
            setStatusBarColorToSlightlyDarkerThan(rgb);

            setTitle(seasons.getShowName());
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private void setStatusBarColorToSlightlyDarkerThan(int rgb) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                return;
            }

            final int darkenByOffset = 20;
            int statusBarColor = Color.rgb(
                    Color.red(rgb) - darkenByOffset >= 0 ? Color.red(rgb) - darkenByOffset : 0,
                    Color.green(rgb) - darkenByOffset >= 0 ? Color.green(rgb) - darkenByOffset : 0,
                    Color.blue(rgb) - darkenByOffset >= 0 ? Color.blue(rgb) - darkenByOffset : 0
            );
            getWindow().setStatusBarColor(statusBarColor);
        }

    }

}
