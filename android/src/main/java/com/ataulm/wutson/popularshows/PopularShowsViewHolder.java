package com.ataulm.wutson.popularshows;

import android.support.v7.widget.RecyclerView;

class PopularShowsViewHolder extends RecyclerView.ViewHolder {

    private final PopularShowsListItemView itemView;

    public PopularShowsViewHolder(PopularShowsListItemView itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    void bind(PopularShow show) {
        itemView.update(show);
    }

}
