package com.ataulm.wutson.popularshows;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.ataulm.wutson.R;
import com.ataulm.wutson.WutsonApplication;
import com.ataulm.wutson.tmdb.TmdbPopularShow;
import com.ataulm.wutson.tmdb.TmdbPopularShows;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PopularShowsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_shows);

        ((WutsonApplication) getApplication()).getDataRepository().getPopularShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer());
    }

    private class Observer implements rx.Observer<TmdbPopularShows> {

        @Override
        public void onCompleted() {
            Log.d("THING", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            throw new Error(e);
        }

        @Override
        public void onNext(TmdbPopularShows tmdbPopularShows) {
            Log.d("THING", "onCompleted");
            for (TmdbPopularShow tmdbPopularShow : tmdbPopularShows) {
                Log.d("THING", tmdbPopularShow.toString());
            }
        }

    }

}
