package com.ataulm.wutson.show;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;

import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.navigation.WutsonActivity;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShowDetailsActivity extends WutsonActivity {

    public static final String TMDB_SHOW_ID = "TMDB_SHOW_ID";

    private Subscription showDetailsSubscription;
    private ShowView showView;

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
            Palette.Swatch swatch = Jabber.swatches().get(show.getPosterUri());
            getToolbar().setBackgroundColor(swatch.getRgb());
            showView.display(show);

            setTitle(show.getName());
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

    }

}
