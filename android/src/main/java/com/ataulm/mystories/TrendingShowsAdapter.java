package com.ataulm.mystories;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

class TrendingShowsAdapter extends RecyclerView.Adapter<TrendingShowsViewHolder> {

    private static final int VIEW_TYPE_STANDARD = 1;
    private static final int VIEW_TYPE_EMPHASISED = 2;

    private List<Show> shows;

    static TrendingShowsAdapter newInstance() {
        return new TrendingShowsAdapter(Collections.<Show>emptyList());
    }

    TrendingShowsAdapter(List<Show> shows) {
        this.shows = shows;
    }

    public void onUpdate(List<Show> shows) {
        this.shows = shows;
        notifyDataSetChanged();
    }

    @Override
    public TrendingShowsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        TrendingShowsItemView view = (TrendingShowsItemView) layoutInflater.inflate(R.layout.view_trending_shows_list_standard, viewGroup, false);
        return new TrendingShowsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrendingShowsViewHolder viewHolder, int position) {
        Show show = shows.get(position);
        viewHolder.present(show);
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }

}
