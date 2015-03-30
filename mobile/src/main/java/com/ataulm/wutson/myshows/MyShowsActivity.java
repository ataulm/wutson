package com.ataulm.wutson.myshows;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.navigation.NavigationDrawerItem;
import com.ataulm.wutson.navigation.WutsonTopLevelActivity;
import com.ataulm.wutson.rx.LoggingObserver;

import java.util.List;
import java.util.Set;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyShowsActivity extends WutsonTopLevelActivity {

    private TextView myShowsTextView;
    private Subscription trackedShowsSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shows);
        myShowsTextView = (TextView) findViewById(R.id.test);
    }

    @Override
    protected void onResume() {
        super.onResume();
        trackedShowsSubscription = Jabber.dataRepository().getMyShows()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer());
    }

    @Override
    protected void onPause() {
        if (!trackedShowsSubscription.isUnsubscribed()) {
            trackedShowsSubscription.unsubscribe();
        }
        super.onPause();
    }

    @Override
    protected NavigationDrawerItem getNavigationDrawerItem() {
        return NavigationDrawerItem.MY_SHOWS;
    }

    private class Observer extends LoggingObserver<List<ShowSummary>> {

        @Override
        public void onNext(List<ShowSummary> showSummaries) {
            if (nothingToSeeHere(showSummaries)) {
                navigate().toDiscover();
                finish();
            } else {
                updateUiWith(showSummaries);
            }
        }

        private void updateUiWith(List<ShowSummary> showSummaries) {
            // TODO: update UI, with empty view or updated adapter
        }

        private boolean nothingToSeeHere(List<ShowSummary> showSummaries) {
            return activityWasOpenedFromLauncher() && showSummaries.isEmpty();
        }

        private boolean activityWasOpenedFromLauncher() {
            Set<String> categories = getIntent().getCategories();
            return categories != null && categoryLauncherIsIn(categories);
        }

    }

    private static boolean categoryLauncherIsIn(Set<String> categories) {
        return categories.contains(Intent.CATEGORY_LAUNCHER) || categoryLeanbackLauncherIsIn(categories);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static boolean categoryLeanbackLauncherIsIn(Set<String> categories) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && categories.contains(Intent.CATEGORY_LEANBACK_LAUNCHER);
    }

}
