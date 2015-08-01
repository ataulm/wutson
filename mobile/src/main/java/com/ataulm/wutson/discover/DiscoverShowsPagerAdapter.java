package com.ataulm.wutson.discover;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.vpa.ViewPagerAdapter;
import com.ataulm.wutson.R;
import com.ataulm.wutson.shows.ShowSummaries;
import com.ataulm.wutson.shows.discover.DiscoverShows;

final class DiscoverShowsPagerAdapter extends ViewPagerAdapter {

    private final LayoutInflater layoutInflater;
    private final DiscoverShowSummaryInteractionListener listener;
    private final int spanCount;

    private DiscoverShows discoverShows;

    static DiscoverShowsPagerAdapter newInstance(Context context, DiscoverShowSummaryInteractionListener listener) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        int spanCount = context.getResources().getInteger(R.integer.discover_shows_span_count);
        return new DiscoverShowsPagerAdapter(layoutInflater, listener, spanCount);
    }

    private DiscoverShowsPagerAdapter(LayoutInflater layoutInflater, DiscoverShowSummaryInteractionListener listener, int spanCount) {
        this.layoutInflater = layoutInflater;
        this.listener = listener;
        this.spanCount = spanCount;
    }

    public void update(DiscoverShows discoverShows) {
        this.discoverShows = discoverShows;
        notifyDataSetChanged();
    }

    @Override
    protected View getView(ViewGroup viewGroup, int position) {
        RecyclerView recyclerView = (RecyclerView) layoutInflater.inflate(R.layout.view_discover_page, viewGroup, false);
        recyclerView.setLayoutManager(new GridLayoutManager(viewGroup.getContext(), spanCount));

        ShowSummaries showSummaries = discoverShows.getShowSummaries(position);
        recyclerView.setAdapter(new DiscoverShowAdapter(showSummaries, listener));
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
