package com.ataulm.wutson.seasons;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;

class SeasonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Season season;
    private final LayoutInflater inflater;

    SeasonAdapter(Season season, LayoutInflater inflater) {
        this.season = season;
        this.inflater = inflater;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.view_season_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Season.Episode item = season.get(position);
        ((SeasonEpisodeView) holder.itemView).display(item);
    }

    @Override
    public long getItemId(int position) {
        return season.get(position).getEpisodeNumber();
    }

    @Override
    public int getItemCount() {
        return season.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }

    }

}
