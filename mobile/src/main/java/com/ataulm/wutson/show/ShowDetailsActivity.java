package com.ataulm.wutson.show;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.navigation.WutsonActivity;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShowDetailsActivity extends WutsonActivity implements OnClickSeasonListener {

    private Subscription showDetailsSubscription;
    private ShowView showView;
    private String showId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        showView = (ShowView) findViewById(R.id.show_detail_show);
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
        showId = getIntent().getData().getLastPathSegment();
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

    @Override
    public void onClick(Show.Season season) {
        navigate().toSeason(showId, season.getSeasonNumber());
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
            showView.display(show, ShowDetailsActivity.this);

            setTitle(show.getName());
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
