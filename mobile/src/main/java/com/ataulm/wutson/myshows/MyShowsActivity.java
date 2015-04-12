package com.ataulm.wutson.myshows;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.discover.ShowSummaryView;
import com.ataulm.wutson.episodes.Episode;
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
        RecyclerView listView = (RecyclerView) findViewById(R.id.my_shows_list);
        listView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new TrackedShowsAdapter(getLayoutInflater());
        adapter.setHasStableIds(true);
        listView.setAdapter(adapter);

        trackedShowsSubscription = Jabber.dataRepository().getMyShows()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer());
    }

    private static class TrackedShowsAdapter extends RecyclerView.Adapter<TrackedShowsAdapter.TrackedShowsItemViewHolder> {

        private final LayoutInflater layoutInflater;

        private List<ShowSummary> showSummaries;

        TrackedShowsAdapter(LayoutInflater layoutInflater) {
            this.layoutInflater = layoutInflater;
        }

        void update(List<ShowSummary> showSummaries) {
            this.showSummaries = showSummaries;
            notifyDataSetChanged();
        }

        @Override
        public TrackedShowsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return TrackedShowsItemViewHolder.createViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TrackedShowsItemViewHolder holder, int position) {
            holder.bind(showSummaries.get(position));
        }

        @Override
        public int getItemCount() {
            if (showSummaries == null) {
                return 0;
            }
            return showSummaries.size();
        }

        @Override
        public long getItemId(int position) {
            return showSummaries.get(position).getId().hashCode();
        }

        static class TrackedShowsItemViewHolder extends RecyclerView.ViewHolder {

            static TrackedShowsItemViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
                View view = layoutInflater.inflate(R.layout.view_tracked_shows_item, parent, false);
                return new TrackedShowsItemViewHolder(view);
            }

            public TrackedShowsItemViewHolder(View itemView) {
                super(itemView);
            }

            void bind(ShowSummary show) {
                ((ShowSummaryView) itemView).setPoster(show.getPosterUri());
                ((ShowSummaryView) itemView).setTitle(show.getName());
            }

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

    private class Observer extends LoggingObserver<List<ShowSummary>> {

        @Override
        public void onNext(List<ShowSummary> showSummaries) {
            if (nothingToSeeHere(showSummaries)) {
                navigate().toDiscover();
                finish();
            } else {
                setTitle(R.string.my_shows_label);
                updateUiWith(showSummaries);
            }
        }

        private boolean nothingToSeeHere(List<ShowSummary> showSummaries) {
            return activityWasOpenedFromLauncher() && showSummaries.isEmpty();
        }

        private boolean activityWasOpenedFromLauncher() {
            Set<String> categories = getIntent().getCategories();
            return categories != null && categoryLauncherIsIn(categories);
        }

        private void updateUiWith(List<ShowSummary> showSummaries) {
            adapter.update(showSummaries);
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
