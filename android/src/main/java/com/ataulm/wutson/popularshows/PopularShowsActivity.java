package com.ataulm.wutson.popularshows;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.R;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.TmdbApiFactory;
import com.ataulm.wutson.tmdb.TmdbTvShow;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PopularShowsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_shows);

        TmdbApiFactory tmdbApiFactory = TmdbApiFactory.newInstance(BuildConfig.TMDB_API_KEY);
        TmdbApi api = tmdbApiFactory.createApi();
        Observable<TmdbTvShow> show = api.getShow("1399");
        show.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Observer<TmdbTvShow>() {

            @Override
            public void onCompleted() {
                Log.d("THING", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("THING", e.getMessage());
            }

            @Override
            public void onNext(TmdbTvShow tmdbTvShow) {
                Log.d("THING", tmdbTvShow.getName());
                Log.d("THING", "seasons: " + tmdbTvShow.getSeasons().size());
            }

        });
    }

}
