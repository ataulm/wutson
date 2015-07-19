package com.ataulm.wutson.discover;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.vpa.ViewPagerAdapter;
import com.ataulm.wutson.R;
import com.ataulm.wutson.shows.ShowSummaries;
import com.ataulm.wutson.shows.discover.DiscoverShows;

class DiscoverShowsPagerAdapter extends ViewPagerAdapter {

    private static final int SPAN_COUNT = 2;

    private final LayoutInflater layoutInflater;

    private DiscoverShows discoverShows;

    DiscoverShowsPagerAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public void update(DiscoverShows discoverShows) {
        this.discoverShows = discoverShows;
        notifyDataSetChanged();
    }

    @Override
    protected View getView(ViewGroup viewGroup, int position) {
        RecyclerView recyclerView = (RecyclerView) layoutInflater.inflate(R.layout.view_discover_page, viewGroup, false);
        recyclerView.setLayoutManager(new GridLayoutManager(viewGroup.getContext(), SPAN_COUNT));

        ShowSummaries showSummaries = discoverShows.getShowSummaries(position);
        recyclerView.setAdapter(new DiscoverShowAdapter(showSummaries));
        return recyclerView;
    }

    @Override
    public int getCount() {
        if (discoverShows == null) {
            return 0;
        }
        return discoverShows.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return discoverShows.getTitle(position);
    }
}
