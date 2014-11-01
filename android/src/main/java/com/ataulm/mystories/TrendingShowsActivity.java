package com.ataulm.mystories;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class TrendingShowsActivity extends Activity {

    private static final int COLUMN_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_shows);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.trending_shows_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, COLUMN_COUNT));
        TrendingShowsAdapter adapter = TrendingShowsAdapter.newInstance();
        recyclerView.setAdapter(adapter);

        adapter.onUpdate(getTrendingShows());
    }

    private List<Show> getTrendingShows() {
        return new ShowsProvider().fetchTrendingShows();
    }

}
