package com.ataulm.wutson.show;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;

import java.util.List;

public class ShowSeasonsView extends RecyclerView {

    public ShowSeasonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void display(List<Show.Season> seasons) {
        if (seasons.isEmpty()) {
            return;
        }

        RecyclerView.Adapter seasonsAdapter = new SeasonsAdapter(LayoutInflater.from(getContext()), seasons);
        setAdapter(seasonsAdapter);
    }

    private static class SeasonsAdapter extends RecyclerView.Adapter<SeasonsAdapter.SeasonViewHolder> {

        private final LayoutInflater inflater;
        private final List<Show.Season> seasons;

        SeasonsAdapter(LayoutInflater inflater, List<Show.Season> seasons) {
            this.inflater = inflater;
            this.seasons = seasons;
        }

        @Override
        public SeasonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.view_show_seasons_item, parent, false);
            return new SeasonViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SeasonViewHolder holder, int position) {
            Show.Season season = seasons.get(position);
            ((ShowSeasonsItemView) holder.itemView).display(season);
        }

        @Override
        public int getItemCount() {
            return seasons.size();
        }

        static class SeasonViewHolder extends RecyclerView.ViewHolder {

            public SeasonViewHolder(View itemView) {
                super(itemView);
            }

        }

    }

}
