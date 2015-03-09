package com.ataulm.wutson.seasons;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;

public class SeasonView extends RecyclerView {

    public SeasonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setLayoutManager(new LinearLayoutManager(context));
    }

    void display(Season season) {
        super.setAdapter(new SeasonPagerAdapter(season, LayoutInflater.from(getContext())));
    }

    @Override
    public final void setLayoutManager(LayoutManager layout) {
        throw DeveloperError.methodCannotBeCalledOutsideThisClass();
    }

    @Override
    public final void setAdapter(Adapter adapter) {
        throw DeveloperError.methodCannotBeCalledOutsideThisClass();
    }

    private static class SeasonPagerAdapter extends Adapter<SeasonPagerAdapter.ViewHolder> {

        private final Season season;
        private final LayoutInflater inflater;

        SeasonPagerAdapter(Season season, LayoutInflater inflater) {
            this.season = season;
            this.inflater = inflater;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(inflater.inflate(R.layout.view_seasons_episode_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Season.Episode item = season.get(position);
            ((SeasonsEpisodeItemView) holder.itemView).display(item);
        }

        @Override
        public long getItemId(int position) {
            return season.get(position).getEpisodeNumber();
        }

        @Override
        public int getItemCount() {
            return season.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            ViewHolder(View itemView) {
                super(itemView);
            }

        }

    }

}
