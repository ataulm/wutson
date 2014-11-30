package com.ataulm.wutson;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

class TrendingShowsViewHolder extends RecyclerView.ViewHolder {

    private final TrendingShowsItemView trendingShowsItemView;
    private final TextView titleView;

    public TrendingShowsViewHolder(TrendingShowsItemView itemView) {
        super(itemView);
        this.trendingShowsItemView = itemView;
        this.titleView = (TextView) trendingShowsItemView.findViewById(R.id.trending_shows_item_title);
    }

    public void updateWith(final Show show) {
        trendingShowsItemView.setContentDescription(show.getName());

        titleView.setText(show.getName());
    }

}
