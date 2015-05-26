package com.ataulm.wutson.showdetails;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.model.Show;

import java.util.List;

class SeasonsAdapter extends RecyclerView.Adapter<SeasonsAdapter.SeasonViewHolder> {

    private final LayoutInflater inflater;
    private final List<Show.SeasonSummary> seasonSummaries;
    private final OnClickSeasonListener listener;

    SeasonsAdapter(LayoutInflater inflater, List<Show.SeasonSummary> seasonSummaries, OnClickSeasonListener listener) {
        this.inflater = inflater;
        this.seasonSummaries = seasonSummaries;
        this.listener = listener;
    }

    @Override
    public SeasonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SeasonSummaryView view = (SeasonSummaryView) inflater.inflate(R.layout.view_show_seasons_item, parent, false);
        return new SeasonViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(SeasonViewHolder holder, int position) {
        Show.SeasonSummary seasonSummary = seasonSummaries.get(position);
        holder.display(seasonSummary);
    }

    @Override
    public long getItemId(int position) {
        return seasonSummaries.get(position).getId().hashCode();
    }

    @Override
    public int getItemCount() {
        return seasonSummaries.size();
    }

    static class SeasonViewHolder extends RecyclerView.ViewHolder {

        private final SeasonSummaryView seasonSummaryView;
        private final OnClickSeasonListener listener;

        SeasonViewHolder(SeasonSummaryView seasonSummaryView, OnClickSeasonListener listener) {
            super(seasonSummaryView);
            this.seasonSummaryView = seasonSummaryView;
            this.listener = listener;
        }

        void display(final Show.SeasonSummary seasonSummary) {
            seasonSummaryView.setPoster(seasonSummary.getPosterPath().toString());
            seasonSummaryView.setSeasonNumber(seasonSummary.getSeasonNumber());
            seasonSummaryView.setEpisodeCount(seasonSummary.getEpisodeCount());
            seasonSummaryView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onClick(seasonSummary);
                }

            });
        }

    }

}
