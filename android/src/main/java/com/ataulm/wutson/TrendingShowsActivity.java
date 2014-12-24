package com.ataulm.wutson;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class TrendingShowsActivity extends Activity {

    private final Provider<List<Show>> showsProvider;

    TrendingShowsActivity() {
        showsProvider = new ShowsProvider();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_shows);

        setupToolbar();
        assembleTrendingShowsList();
    }

    private void setupToolbar() {
        Toolbar actionBar = (Toolbar) findViewById(R.id.action_bar);
        actionBar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(TrendingShowsActivity.this, "click hamburger", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void assembleTrendingShowsList() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.trending_shows_list);
        recyclerView.setLayoutManager(TrendingShowsLayoutManager.newInstance(this));

        TrendingShowsAdapter adapter = TrendingShowsAdapter.newInstance();
        recyclerView.setAdapter(adapter);

        adapter.onUpdate(getTrendingShows());
    }

    private List<Show> getTrendingShows() {
        return showsProvider.provide();
    }

}
