package com.ataulm.wutson.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.shows.ShowSummaries;

class DiscoverShowAdapter extends RecyclerView.Adapter<DiscoverShowViewHolder> {

    private final ShowSummaries showSummaries;

    DiscoverShowAdapter(ShowSummaries showSummaries) {
        this.showSummaries = showSummaries;
        super.setHasStableIds(true);
    }

    @Override
    public final void setHasStableIds(boolean hasStableIds) {
        throw DeveloperError.methodCannotBeCalledOutsideThisClass();
    }

    @Override
    public DiscoverShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return DiscoverShowViewHolder.inflate(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(DiscoverShowViewHolder holder, int position) {
        holder.bind(showSummaries.get(position));
    }

    @Override
    public int getItemCount() {
        return showSummaries.size();
    }

    @Override
    public long getItemId(int position) {
        return showSummaries.get(position).getId().hashCode();
    }

}
