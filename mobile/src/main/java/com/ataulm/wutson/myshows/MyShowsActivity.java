package com.ataulm.wutson.myshows;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ataulm.rv.SpacesItemDecoration;
import com.ataulm.wutson.R;
import com.ataulm.wutson.discover.OnShowClickListener;
import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.navigation.NavigationDrawerItem;
import com.ataulm.wutson.navigation.WutsonTopLevelActivity;
import com.ataulm.wutson.rx.LoggingObserver;
import com.ataulm.wutson.shows.ShowSummaries;
import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.shows.TrackedStatus;

import java.util.Set;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MyShowsActivity extends WutsonTopLevelActivity implements OnShowClickListener {

    private TrackedShowsAdapter trackedShowsAdapter;

    private CompositeSubscription subscriptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideTitleWhileWeCheckForTrackedShows();
        setContentView(R.layout.activity_my_shows);

        trackedShowsAdapter = new TrackedShowsAdapter(this);
        trackedShowsAdapter.setHasStableIds(true);
        RecyclerView showsListView = (RecyclerView) findViewById(R.id.my_shows_list);
        int spanCount = getResources().getInteger(R.integer.my_shows_span_count);
        showsListView.setLayoutManager(new GridLayoutManager(this, spanCount));
        int spacing = getResources().getDimensionPixelSize(R.dimen.my_shows_item_spacing);
        showsListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, spanCount));
        showsListView.setAdapter(trackedShowsAdapter);

        subscriptions = new CompositeSubscription(
                Jabber.dataRepository().getMyShows()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new TrackedShowsObserver())
        );
    }

    private void hideTitleWhileWeCheckForTrackedShows() {
        setTitle(null);
    }

    @Override
    protected void onDestroy() {
        subscriptions.clear();
        super.onDestroy();
    }

    @Override
    protected NavigationDrawerItem getNavigationDrawerItem() {
        return NavigationDrawerItem.MY_SHOWS;
    }

    @Override
    public void onClick(ShowSummary showSummary) {
        navigate().toShowDetails(showSummary.getId(), showSummary.getName(), showSummary.getBackdropUri().toString());
    }

    @Override
    public void onClickStopTracking(ShowSummary showSummary) {
        Jabber.dataRepository().setTrackedStatus(showSummary.getId(), TrackedStatus.NOT_TRACKED);
    }

    @Override
    public void onClickTrack(ShowSummary showSummary) {
        Jabber.dataRepository().setTrackedStatus(showSummary.getId(), TrackedStatus.TRACKED);
    }

    private class TrackedShowsObserver extends LoggingObserver<ShowSummaries> {

        private boolean firstLoad = true;

        private TrackedShowsObserver() {
            super(Jabber.log());
        }

        @Override
        public void onNext(ShowSummaries showSummaries) {
            super.onNext(showSummaries);
            if (nothingToSeeHere(showSummaries)) {
                navigate().toDiscover();
            } else {
                // TODO this is broken - open with no shows -> discover, backs into this activity (should close app)
                firstLoad = false;
                setTitle(R.string.my_shows_label);
                trackedShowsAdapter.update(showSummaries);
            }
        }

        private boolean nothingToSeeHere(ShowSummaries showSummaries) {
            return showSummaries.size() == 0 && activityWasOpenedFromLauncher() && firstLoad;
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
