package com.ataulm.wutson.show;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;

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
        showView = (ShowView) findViewById(R.id.show_details_show);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        customiseShowDetailsToolbar();
    }

    private void customiseShowDetailsToolbar() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getToolbar().getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        getToolbar().getBackground().setAlpha(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDetailsSubscription = getDataRepository().getShow(getIntent().getStringExtra(TMDB_SHOW_ID))
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

            ShowView.ScrollListener scrollListener = ShowViewScrollListener.newInstance(getResources(), getToolbar().getNavigationIcon(), getToolbar().getBackground());
            showView.setScrollListener(scrollListener);
            showView.display(show);
        }

    }

}
