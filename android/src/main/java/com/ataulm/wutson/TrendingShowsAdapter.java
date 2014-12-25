package com.ataulm.wutson;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

class TrendingShowsAdapter extends RecyclerView.Adapter<TrendingShowsViewHolder> {

    private Shows shows;

    static TrendingShowsAdapter newInstance() {
        return new TrendingShowsAdapter(Shows.empty());
    }

    TrendingShowsAdapter(Shows shows) {
        this.shows = shows;
    }

    public void onUpdate(Shows shows) {
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
        viewHolder.updateWith(show);
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }

}
