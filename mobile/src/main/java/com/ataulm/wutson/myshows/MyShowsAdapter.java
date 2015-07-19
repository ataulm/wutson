package com.ataulm.wutson.myshows;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ataulm.wutson.DataSet;
import com.ataulm.wutson.shows.ShowSummary;

class MyShowsAdapter extends RecyclerView.Adapter<ShowSummaryViewHolder> {

    private final OnShowClickListener listener;
    private final DataSet<ShowSummary> dataSet;
    private final LayoutInflater layoutInflater;

    MyShowsAdapter(DataSet<ShowSummary> dataSet, OnShowClickListener listener, LayoutInflater layoutInflater) {
        this.dataSet = dataSet;
        this.listener = listener;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public ShowSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ShowSummaryViewHolder.inflate(parent, layoutInflater);
    }

    @Override
    public void onBindViewHolder(ShowSummaryViewHolder holder, int position) {
        ShowSummary item = dataSet.getItem(position);
        holder.bind(item, listener);
    }

    @Override
    public long getItemId(int position) {
        return dataSet.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return dataSet.getItemCount();
    }

}
