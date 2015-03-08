package com.ataulm.wutson.seasons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.ataulm.wutson.R;

public class SeasonView extends ListView {

    public SeasonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void display(Season season) {
        setAdapter(new SeasonPagerAdapter(season, LayoutInflater.from(getContext())));
    }

    private static class SeasonPagerAdapter extends BaseAdapter {

        private final Season season;
        private final LayoutInflater inflater;

        SeasonPagerAdapter(Season season, LayoutInflater inflater) {
            this.season = season;
            this.inflater = inflater;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public int getCount() {
            return season.size();
        }

        @Override
        public Season.Episode getItem(int position) {
            return season.get(position);
        }

        @Override
        public long getItemId(int position) {
            return season.get(position).getEpisodeNumber();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.view_seasons_episode_item, parent, false);
            }
            ((SeasonsEpisodeItemView) view).display(getItem(position));
            return view;
        }

    }

}
