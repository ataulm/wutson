package com.ataulm.wutson.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;
import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.view.ShowSummaryView;

class DiscoverShowViewHolder extends RecyclerView.ViewHolder {

    private final DiscoverShowSummaryInteractionListener listener;

    static DiscoverShowViewHolder inflate(LayoutInflater layoutInflater, ViewGroup parent, DiscoverShowSummaryInteractionListener listener) {
        View view = layoutInflater.inflate(R.layout.view_discover_show, parent, false);
        return new DiscoverShowViewHolder(view, listener);
    }

    private DiscoverShowViewHolder(View itemView, DiscoverShowSummaryInteractionListener listener) {
        super(itemView);
        this.listener = listener;
    }

    public void bind(final ShowSummary showSummary) {
        ((ShowSummaryView) itemView).setTitle(showSummary.getName());
        ((ShowSummaryView) itemView).setPoster(showSummary.getPosterUri());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(showSummary);
            }
        });
    }

}
