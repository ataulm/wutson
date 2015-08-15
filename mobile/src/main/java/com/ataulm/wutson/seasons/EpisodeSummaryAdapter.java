package com.ataulm.wutson.seasons;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;
import com.ataulm.wutson.episodes.Episode;

class EpisodeSummaryAdapter extends RecyclerView.Adapter<EpisodeSummaryAdapter.EpisodeSummaryViewHolder> {

    private final Season season;
    private final OnClickEpisodeListener listener;
    private final LayoutInflater inflater;

    EpisodeSummaryAdapter(Season season, OnClickEpisodeListener listener, LayoutInflater inflater) {
        this.season = season;
        this.listener = listener;
        this.inflater = inflater;
    }

    @Override
    public EpisodeSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.view_season_item, parent, false);
        return new EpisodeSummaryViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(EpisodeSummaryViewHolder viewHolder, int position) {
        Episode episode = season.get(position);
        viewHolder.display(episode);
    }

    @Override
    public long getItemId(int position) {
        return season.get(position).getSeasonEpisodeNumber().getEpisode();
    }

    @Override
    public int getItemCount() {
        return season.size();
    }

    static class EpisodeSummaryViewHolder extends RecyclerView.ViewHolder {

        private final OnClickEpisodeListener listener;

        EpisodeSummaryViewHolder(View itemView, OnClickEpisodeListener listener) {
            super(itemView);
            this.listener = listener;
        }

        void display(final Episode episode) {
            ((EpisodeSummaryView) itemView).display(episode);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onClick(episode);
                }

            });
        }

    }

}
