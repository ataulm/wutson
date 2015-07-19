package com.ataulm.wutson.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;
import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.view.ShowSummaryView;

class DiscoverShowViewHolder extends RecyclerView.ViewHolder {

    static DiscoverShowViewHolder inflate(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.view_discover_show, parent, false);
        return new DiscoverShowViewHolder(view);
    }

    private DiscoverShowViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(ShowSummary showSummary) {
        ((ShowSummaryView) itemView).setTitle(showSummary.getName());
        ((ShowSummaryView) itemView).setPoster(showSummary.getPosterUri());
    }

}
