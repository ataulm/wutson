package com.ataulm.mystories;

import android.support.v7.widget.RecyclerView;

class TrendingShowsViewHolder extends RecyclerView.ViewHolder {

    private final TrendingShowsItemView trendingShowsItemView;

    public TrendingShowsViewHolder(TrendingShowsItemView itemView) {
        super(itemView);
        this.trendingShowsItemView = itemView;
    }

    public void present(Show show) {
        // TODO: update view with model
    }

}
