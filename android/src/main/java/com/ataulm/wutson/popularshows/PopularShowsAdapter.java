package com.ataulm.wutson.popularshows;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;

import java.util.Collections;

class PopularShowsAdapter extends RecyclerView.Adapter<PopularShowsViewHolder> {

    private PopularShows popularShows = new PopularShows(Collections.<PopularShow>emptyList());

    @Override
    public PopularShowsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_popular_shows_list_item, parent, false);
        return new PopularShowsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PopularShowsViewHolder holder, int position) {
        PopularShow show = popularShows.get(position);
        holder.bind(show);
    }

    @Override
    public int getItemCount() {
        return popularShows.size();
    }

    void update(PopularShows popularShows) {
        this.popularShows = popularShows;
        notifyDataSetChanged();
    }

}
