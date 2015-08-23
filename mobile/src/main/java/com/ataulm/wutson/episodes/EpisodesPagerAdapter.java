package com.ataulm.wutson.episodes;

import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ataulm.vpa.ViewPagerAdapter;
import com.ataulm.wutson.R;
import com.ataulm.wutson.seasons.Season;
import com.bumptech.glide.Glide;

import java.net.URI;

class EpisodesPagerAdapter extends ViewPagerAdapter {

    private final LayoutInflater layoutInflater;

    @ColorInt
    private final int accentColor;

    private Season season;

    EpisodesPagerAdapter(LayoutInflater layoutInflater, @ColorInt int accentColor) {
        this.layoutInflater = layoutInflater;
        this.accentColor = accentColor;
    }

    void update(Season season) {
        this.season = season;
        notifyDataSetChanged();
    }

    @Override
    protected View getView(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.view_episode_details_page, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.episode_details_image_poster);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.episode_details_recycler_view);
        recyclerView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        Episode episode = season.get(position);
        Details details = Details.from(episode);
        RecyclerView.Adapter adapter = new EpisodeDetailsAdapter(details, layoutInflater, accentColor);

        recyclerView.setAdapter(adapter);
        setEpisodePoster(imageView, episode.getStillPath());
        return view;
    }

    private void setEpisodePoster(ImageView episodePosterView, URI uri) {
        episodePosterView.setImageBitmap(null);

        Glide.with(episodePosterView.getContext())
                .load(uri.toString())
                .error(R.drawable.ic_hero_image_placeholder)
                .into(episodePosterView);
    }

    @Override
    public int getCount() {
        if (season == null) {
            return 0;
        }
        return season.size();
    }

    int positionOfEpisodeNumber(int episodeNumber) {
        for (int i = 0; i < season.size(); i++) {
            if (season.get(i).getSeasonEpisodeNumber().getEpisode() == episodeNumber) {
                return i;
            }
        }
        return 0;
    }

}
