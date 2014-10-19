package com.ataulm.mystories;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ataulm.mystories.discovertv.DiscoverTv;
import com.ataulm.mystories.discovertv.DiscoverTvParser;
import com.ataulm.mystories.discovertv.MockDiscoverTv;

public class DiscoverTvActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_tv);

        setupDiscoverTvList();
    }

    private void setupDiscoverTvList() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.discover_tv_list);
        DiscoverTvAdapter discoverTvAdapter = DiscoverTvAdapter.newInstance();
        recyclerView.setAdapter(discoverTvAdapter);

        DiscoverTvParser parser = DiscoverTvParser.newInstance();
        DiscoverTv discoverTv = parser.parse(MockDiscoverTv.JSON);
        discoverTvAdapter.updateWith(discoverTv);
    }

    private static class DiscoverTvAdapter extends RecyclerView.Adapter {

        private DiscoverTv shows;

        static DiscoverTvAdapter newInstance() {
            return new DiscoverTvAdapter(DiscoverTv.empty());
        }

        DiscoverTvAdapter(DiscoverTv shows) {
            this.shows = shows;
        }

        public void updateWith(DiscoverTv shows) {
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
