package com.ataulm.wutson.trackedshows;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;

import java.util.Collections;

class TrackedShowsAdapter extends RecyclerView.Adapter<TrackedShowsViewHolder> {

    private TrackedShows trackedShows = new TrackedShows(Collections.<TrackedShow>emptyList());

    @Override
    public TrackedShowsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_tracked_shows_list_item, parent, false);
        return new TrackedShowsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackedShowsViewHolder holder, int position) {
        TrackedShow trackedShow = trackedShows.get(position);
        holder.bind(trackedShow);
    }

    @Override
    public int getItemCount() {
        return trackedShows.size();
    }

    void update(TrackedShows trackedShows) {
        this.trackedShows = trackedShows;
        notifyDataSetChanged();
    }

}
