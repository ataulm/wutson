package com.ataulm.wutson.episodes;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.navigation.WutsonActivity;
import com.ataulm.wutson.rx.LoggingObserver;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EpisodeDetailsActivity extends WutsonActivity {

    private Subscription episodesSubscription;
    private ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_details);
        pager = (ViewPager) findViewById(R.id.episodes_pager);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Drawable navigationIcon = getToolbar().getNavigationIcon();
        if (navigationIcon != null) {
            navigationIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        episodesSubscription = Jabber.dataRepository().getEpisodes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer());
    }

    @Override
    protected void onPause() {
        if (!episodesSubscription.isUnsubscribed()) {
            episodesSubscription.unsubscribe();
        }
        super.onPause();
    }

    private class Observer extends LoggingObserver<Episodes> {

        @Override
        public void onNext(Episodes episodes) {
            // update adapter
            setTitle(episodes.getShowName());
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

    }

}
