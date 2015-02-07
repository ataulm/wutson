package com.ataulm.wutson.show;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;

import com.ataulm.wutson.R;
import com.ataulm.wutson.model.TvShow;
import com.ataulm.wutson.navigation.WutsonActivity;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShowDetailsActivity extends WutsonActivity {

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
        showDetailsSubscription = getDataRepository().getTvShow("1973-24")
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

    private class Observer implements rx.Observer<TvShow> {

        @Override
        public void onCompleted() {
            Log.d("THING", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            throw new Error(e);
        }

        @Override
        public void onNext(TvShow tvShow) {
            Log.d("THING", "onCompleted");
            Log.d("THING", tvShow.getName());
            Log.d("THING", tvShow.getPosterPath());
            Log.d("THING", tvShow.getOverview());
            for (TvShow.Season season : tvShow.getSeasons()) {
                Log.d("THING", season.toString());
            }

            ShowView.ScrollListener scrollListener = ShowViewScrollListener.newInstance(getResources(), getToolbar().getNavigationIcon(), getToolbar().getBackground());
            showView.setScrollListener(scrollListener);
            showView.display(new DummyShowMaker().getDummyShow());
        }

    }

}
