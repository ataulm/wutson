package com.ataulm.wutson.browseshows;

import android.os.Bundle;
import android.util.Log;

import com.ataulm.wutson.R;
import com.ataulm.wutson.navigation.WutsonActivity;
import com.ataulm.wutson.model.TvShow;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TvShowActivity extends WutsonActivity {

    private Subscription tvShowSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvShowSubscription = getDataRepository().getTvShow("1973-24")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer());
    }

    @Override
    protected void onPause() {
        if (!tvShowSubscription.isUnsubscribed()) {
            tvShowSubscription.unsubscribe();
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
        }

    }

}
