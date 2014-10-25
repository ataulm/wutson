package com.ataulm.mystories;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

public class TrendingShowsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_shows);

        setupTrendingTvShowsList();
    }

    private void setupTrendingTvShowsList() {
        RecyclerView trendingShowsList = (RecyclerView) findViewById(R.id.trending_shows_list);
        TrendingShowsAdapter trendingShowsAdapter = TrendingShowsAdapter.newInstance();
        trendingShowsList.setAdapter(trendingShowsAdapter);

        List<Show> trendingShows = getTrendingShows();
        trendingShowsAdapter.updateWith(trendingShows);
    }

    private List<Show> getTrendingShows() {
        ShowsProvider showsProvider = new ShowsProvider();
        return showsProvider.fetchTrendingShows();
    }

    private static class TrendingShowsAdapter extends RecyclerView.Adapter {

        private List<Show> shows;

        static TrendingShowsAdapter newInstance() {
            return new TrendingShowsAdapter(Collections.<Show>emptyList());
        }

        TrendingShowsAdapter(List<Show> shows) {
            this.shows = shows;
        }

        public void updateWith(List<Show> shows) {
            this.shows = shows;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            // TODO: create view holder
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            // TODO: bind data to view?
        }

        @Override
        public int getItemCount() {
            return shows.size();
        }

    }

}
