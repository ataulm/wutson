package com.ataulm.wutson.myshows;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ataulm.rv.SpacesItemDecoration;
import com.ataulm.vpa.ViewPagerAdapter;
import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.discover.OnShowClickListener;
import com.ataulm.wutson.model.EpisodesByDate;
import com.ataulm.wutson.model.ShowSummaries;
import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.navigation.NavigationDrawerItem;
import com.ataulm.wutson.navigation.WutsonTopLevelActivity;
import com.ataulm.wutson.rx.LoggingObserver;
import com.novoda.landingstrip.LandingStrip;

import java.util.Set;

import rx.Subscription;

public class MyShowsActivity extends WutsonTopLevelActivity implements OnShowClickListener {

    private static final String KEY_SAVED_STATE = BuildConfig.APPLICATION_ID + ".KEY_SAVED_STATE";

    private Subscription trackedShowsSubscription;
    private Subscription upcomingShowsSubscription;
    private TrackedShowsAdapter adapter;
    private RecyclerView showsView;
    private SparseArray<Parcelable> savedStateForShowsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setContentView(R.layout.activity_my_shows);

        ViewPager viewPager = (ViewPager) findViewById(R.id.my_shows_pager);
        viewPager.setAdapter(new MyShowsPager(getLayoutInflater(), getResources()));

        LandingStrip tabStrip = (LandingStrip) findViewById(R.id.tab_strip);
        tabStrip.attach(viewPager);


//        bindNewTrackedShowsAdapterToShowsView();
//
//        trackedShowsSubscription = Jabber.dataRepository().getMyShows()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Observer());
//
//        upcomingShowsSubscription = Jabber.dataRepository().getUpcomingEpisodes()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new UpcomingObserver());
    }

    private static class MyShowsPager extends ViewPagerAdapter {

        private final LayoutInflater layoutInflater;
        private final Resources resources;

        MyShowsPager(LayoutInflater layoutInflater, Resources resources) {
            this.layoutInflater = layoutInflater;
            this.resources = resources;
        }

        @Override
        protected View getView(ViewGroup viewGroup, int position) {
            Page page = Page.values()[position];
            TextView pageView = (TextView) layoutInflater.inflate(page.getLayoutResId(), viewGroup, false);
            pageView.setText(getPageTitle(position));
            return pageView;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Page.values()[position].getTitle(resources);
        }

        @Override
        public int getCount() {
            return Page.values().length;
        }

        private enum Page {

            ALL(R.layout.view_my_shows_page_all, R.string.my_shows_page_all),
            UPCOMING(R.layout.view_my_shows_page_upcoming, R.string.my_shows_page_upcoming),
            RECENT(R.layout.view_my_shows_page_recent, R.string.my_shows_page_recent);

            @LayoutRes
            private final int layoutResId;

            @StringRes
            private final int titleResId;

            Page(@LayoutRes int layoutResId, @StringRes int titleResId) {
                this.layoutResId = layoutResId;
                this.titleResId = titleResId;
            }

            @LayoutRes int getLayoutResId() {
                return layoutResId;
            }

            String getTitle(Resources resources) {
                if (this == UPCOMING || this == RECENT) {
                    return "unimplemented";
                }
                return resources.getString(titleResId);
            }

        }

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

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        SparseArray<Parcelable> container = new SparseArray<>();
//        showsView.saveHierarchyState(container);
//        outState.putSparseParcelableArray(KEY_SAVED_STATE, container);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        if (savedInstanceState.containsKey(KEY_SAVED_STATE)) {
//            savedStateForShowsView = savedInstanceState.getSparseParcelableArray(KEY_SAVED_STATE);
//        }
//    }

    @Override
    protected void onDestroy() {
//        if (!trackedShowsSubscription.isUnsubscribed()) {
//            trackedShowsSubscription.unsubscribe();
//        }
//        if (!upcomingShowsSubscription.isUnsubscribed()) {
//            upcomingShowsSubscription.unsubscribe();
//        }
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

    private static class UpcomingObserver extends LoggingObserver<EpisodesByDate> {

        @Override
        public void onNext(EpisodesByDate episodes) {
            super.onNext(episodes);
            // TODO: make it a list ready for adapter
        }

    }

    private class Observer extends LoggingObserver<ShowSummaries> {

        private boolean firstOpen = true;

        @Override
        public void onNext(ShowSummaries showSummaries) {
            super.onNext(showSummaries);
            if (firstOpen && nothingToSeeHere(showSummaries)) {
                navigate().toDiscover();
                finish();
            } else {
                firstOpen = false;
                setTitle(R.string.my_shows_label);
                adapter.update(showSummaries);
                if (savedStateForShowsView != null) {
                    showsView.restoreHierarchyState(savedStateForShowsView);
                }
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
