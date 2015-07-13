package com.ataulm.wutson.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.shows.discover.ShowsInGenre;

public class ShowsInGenreAdapter extends RecyclerView.Adapter<ShowSummaryViewHolder> {

    private final OnClickShowSummaryListener onClickShowSummaryListener;
    private final LayoutInflater layoutInflater;

    private ShowsInGenre showsInGenre;

    ShowsInGenreAdapter(LayoutInflater layoutInflater, OnClickShowSummaryListener onClickShowSummaryListener) {
        this.onClickShowSummaryListener = onClickShowSummaryListener;
        this.layoutInflater = layoutInflater;
    }

    void update(ShowsInGenre showsInGenre) {
        this.showsInGenre = showsInGenre;
        notifyDataSetChanged();
    }

    @Override
    public ShowSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ShowSummaryViewHolder.inflate(layoutInflater, parent, onClickShowSummaryListener);
    }

    @Override
    public void onBindViewHolder(ShowSummaryViewHolder holder, int position) {
        ShowSummary showSummary = showsInGenre.get(position);
        holder.update(showSummary);
    }

    @Override
    public long getItemId(int position) {
        return showsInGenre.get(position).getId().hashCode();
    }

    @Override
    public int getItemCount() {
        return showsInGenre.size();
    }

}
