package com.ataulm.wutson.show;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;

import java.util.List;

class SeasonsAdapter extends RecyclerView.Adapter<SeasonsAdapter.SeasonViewHolder> {

    private final LayoutInflater inflater;
    private final List<Show.Season> seasons;
    private final OnClickSeasonListener listener;

    SeasonsAdapter(LayoutInflater inflater, List<Show.Season> seasons, OnClickSeasonListener listener) {
        this.inflater = inflater;
        this.seasons = seasons;
        this.listener = listener;
    }

    @Override
    public SeasonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SeasonSummaryView view = (SeasonSummaryView) inflater.inflate(R.layout.view_show_seasons_item, parent, false);
        return new SeasonViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(SeasonViewHolder holder, int position) {
        Show.Season season = seasons.get(position);
        holder.display(season);
    }

    @Override
    public int getItemCount() {
        return seasons.size();
    }

    static class SeasonViewHolder extends RecyclerView.ViewHolder {

        private final SeasonSummaryView seasonSummaryView;
        private final OnClickSeasonListener listener;

        SeasonViewHolder(SeasonSummaryView seasonSummaryView, OnClickSeasonListener listener) {
            super(seasonSummaryView);
            this.seasonSummaryView = seasonSummaryView;
            this.listener = listener;
        }

        void display(final Show.Season season) {
            seasonSummaryView.setPoster(season.getPosterPath().toString());
            seasonSummaryView.setSeasonNumber(season.getSeasonNumber());
            seasonSummaryView.setEpisodeCount(season.getEpisodeCount());
            seasonSummaryView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onClick(season);
                }

            });
        }

    }

}
