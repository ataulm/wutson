package com.ataulm.wutson.show;

import android.os.Bundle;
import android.util.Log;

import com.ataulm.wutson.Displayer;
import com.ataulm.wutson.Displayers;
import com.ataulm.wutson.R;
import com.ataulm.wutson.model.TvShow;
import com.ataulm.wutson.navigation.WutsonActivity;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShowDetailsActivity extends WutsonActivity {

    private Subscription showDetailsSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        Show arrow = new DummyShowMaker().getDummyShow();
        Displayer<Show> showDisplayer = Displayers.findById(this, R.id.show_details_show);
        showDisplayer.display(arrow);
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
        }

    }

}
