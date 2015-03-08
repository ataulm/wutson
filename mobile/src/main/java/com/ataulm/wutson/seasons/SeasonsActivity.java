package com.ataulm.wutson.seasons;

import android.net.Uri;
import android.os.Bundle;
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
        }

    }

}
