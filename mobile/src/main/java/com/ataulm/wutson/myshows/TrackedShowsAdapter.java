package com.ataulm.wutson.myshows;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ataulm.wutson.ToastDisplayer;
import com.ataulm.wutson.discover.OnShowClickListener;
import com.ataulm.wutson.model.ShowSummaries;

class TrackedShowsAdapter extends RecyclerView.Adapter<ShowSummaryViewHolder> {

    private final OnShowClickListener listener;
    private final ToastDisplayer toaster;

    private ShowSummaries showSummaries;

    TrackedShowsAdapter(OnShowClickListener listener, ToastDisplayer toaster) {
        this.listener = listener;
        this.toaster = toaster;
    }

    void update(ShowSummaries showSummaries) {
        this.showSummaries = showSummaries;
        notifyDataSetChanged();
    }

    @Override
    public ShowSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ShowSummaryViewHolder.inflate(parent, toaster);
    }

    @Override
    public void onBindViewHolder(ShowSummaryViewHolder holder, int position) {
        holder.bind(showSummaries.get(position), listener);
    }

    @Override
    public long getItemId(int position) {
        return showSummaries.get(position).getId().hashCode();
    }

    @Override
    public int getItemCount() {
        if (showSummaries == null) {
            return 0;
        }
        return showSummaries.size();
    }

}
