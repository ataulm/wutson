package com.ataulm.wutson.myshows;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

import com.ataulm.rv.SpacesItemDecoration;
import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.discover.OnShowClickListener;
import com.ataulm.wutson.model.ShowSummaries;
import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.navigation.NavigationDrawerItem;
import com.ataulm.wutson.navigation.WutsonTopLevelActivity;
import com.ataulm.wutson.rx.LoggingObserver;

import java.util.List;
import java.util.Set;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyShowsActivity extends WutsonTopLevelActivity implements OnShowClickListener {

    private static final String KEY_SAVED_STATE = BuildConfig.APPLICATION_ID + ".KEY_SAVED_STATE";

    private Subscription trackedShowsSubscription;
    private TrackedShowsAdapter adapter;
    private RecyclerView showsView;
    private SparseArray<Parcelable> savedStateForShowsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setContentView(R.layout.activity_my_shows);
        bindNewTrackedShowsAdapterToShowsView();

        trackedShowsSubscription = Jabber.dataRepository().getMyShows()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer());
    }

    private void bindNewTrackedShowsAdapterToShowsView() {
        adapter = new TrackedShowsAdapter(this, Jabber.toastDisplayer());
        adapter.setHasStableIds(true);

        final int spanCount = getResources().getInteger(R.integer.my_shows_span_count);
        int spacing = getResources().getDimensionPixelSize(R.dimen.my_shows_item_spacing);
        RecyclerView.ItemDecoration itemDecoration = SpacesItemDecoration.newInstance(spacing, spacing, spanCount);
        showsView = (RecyclerView) findViewById(R.id.my_shows_list);
        showsView.setLayoutManager(new GridLayoutManager(this, spanCount));
        showsView.addItemDecoration(itemDecoration);
        showsView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SparseArray<Parcelable> container = new SparseArray<>();
        showsView.saveHierarchyState(container);
        outState.putSparseParcelableArray(KEY_SAVED_STATE, container);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(KEY_SAVED_STATE)) {
            savedStateForShowsView = savedInstanceState.getSparseParcelableArray(KEY_SAVED_STATE);
        }
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

    @Override
    public void onClick(ShowSummary showSummary) {
        navigate().toShowDetails(showSummary.getId(), showSummary.getName(), showSummary.getBackdropUri().toString());
    }

    private class Observer extends LoggingObserver<ShowSummaries> {

        @Override
        public void onNext(ShowSummaries showSummaries) {
            if (nothingToSeeHere(showSummaries)) {
                navigate().toDiscover();
                finish();
            } else {
                setTitle(R.string.my_shows_label);
                adapter.update(showSummaries);
                showsView.restoreHierarchyState(savedStateForShowsView);
            }
        }

        private boolean nothingToSeeHere(ShowSummaries showSummaries) {
            return showSummaries.size() == 0 && activityWasOpenedFromLauncher();
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
