package com.ataulm.wutson.myshows;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ataulm.rv.SpacesItemDecoration;
import com.ataulm.rv.SpanSizeLookup;
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

    private Subscription trackedShowsSubscription;
    private TrackedShowsAdapter adapter;

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
        adapter = new TrackedShowsAdapter();
        adapter.setHasStableIds(true);

        final int spanCount = getResources().getInteger(R.integer.my_shows_span_count);
        int spacing = getResources().getDimensionPixelSize(R.dimen.my_shows_item_spacing);
        SpacesItemDecoration itemDecoration = SpacesItemDecoration.newInstance(spacing, spacing,
                new SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int i) {
                        return 1;
                    }

                    @Override
                    public int getSpanCount() {
                        return spanCount;
                    }
                }
        );

        RecyclerView showsView = (RecyclerView) findViewById(R.id.my_shows_list);
        showsView.setLayoutManager(new GridLayoutManager(this, spanCount));
        showsView.addItemDecoration(itemDecoration);
        showsView.setAdapter(adapter);
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
