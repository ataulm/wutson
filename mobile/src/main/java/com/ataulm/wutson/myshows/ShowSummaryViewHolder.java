package com.ataulm.wutson.myshows;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;
import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.view.ShowSummaryView;

final class ShowSummaryViewHolder extends RecyclerView.ViewHolder {

    static ShowSummaryViewHolder inflate(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_tracked_shows_item, parent, false);
        return new ShowSummaryViewHolder(view);
    }

    private ShowSummaryViewHolder(View itemView) {
        super(itemView);
    }

    void bind(ShowSummary show) {
        ((ShowSummaryView) itemView).setTitle(show.getName());
        ((ShowSummaryView) itemView).setPoster(show.getPosterUri());
    }

}
