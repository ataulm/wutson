package com.ataulm.wutson.myshows;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.GridView;

import com.ataulm.wutson.BuildConfig;
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

    private static final String KEY_SHOWS_LIST_STATE = BuildConfig.APPLICATION_ID + ".KEY_SHOWS_LIST_STATE";

    private Subscription trackedShowsSubscription;
    private TrackedShowsAdapter adapter;
    private GridView showsList;
    private Parcelable showsListState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setContentView(R.layout.activity_my_shows);
        showsList = (GridView) findViewById(R.id.my_shows_list);
        showsList.setAdapter(adapter = new TrackedShowsAdapter(getLayoutInflater()));

        trackedShowsSubscription = Jabber.dataRepository().getMyShows()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        showsListState = savedInstanceState.getParcelable(KEY_SHOWS_LIST_STATE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_SHOWS_LIST_STATE, showsList.onSaveInstanceState());
    }

    @Override
    protected void onDestroy() {
        if (!trackedShowsSubscription.isUnsubscribed()) {
            trackedShowsSubscription.unsubscribe();
        }
        super.onDestroy();
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
                setTitle(R.string.my_shows_label);
                adapter.update(showSummaries);
                showsList.onRestoreInstanceState(showsListState);
            }
        }

        private boolean nothingToSeeHere(List<ShowSummary> showSummaries) {
            return showSummaries.isEmpty() && activityWasOpenedFromLauncher();
        }

        private boolean activityWasOpenedFromLauncher() {
            Set<String> categories = getIntent().getCategories();
            return categories != null && categoryLauncherIsIn(categories);
        }

        private boolean categoryLauncherIsIn(Set<String> categories) {
            return categories.contains(Intent.CATEGORY_LAUNCHER) || categoryLeanbackLauncherIsIn(categories);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private boolean categoryLeanbackLauncherIsIn(Set<String> categories) {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && categories.contains(Intent.CATEGORY_LEANBACK_LAUNCHER);
        }

    }

}
