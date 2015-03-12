package com.ataulm.wutson.seasons;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.navigation.WutsonActivity;
import com.ataulm.wutson.rx.LoggingObserver;

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

    private class Observer extends LoggingObserver<Seasons> {

        @Override
        public void onNext(Seasons seasons) {
            seasonsView.display(seasons);

            setTitle(seasons.getShowName());
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

    }

}
