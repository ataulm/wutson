package com.ataulm.wutson.episodes;

import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.vpa.ViewPagerAdapter;
import com.ataulm.wutson.R;
import com.ataulm.wutson.seasons.Season;

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
        EpisodeDetailsView view = (EpisodeDetailsView) layoutInflater.inflate(R.layout.view_episode_details_page, container, false);
        Episode episode = season.get(position);

        view.setEpisodeName(episode.getName());
        view.setEpisodePoster(episode.getStillPath());
        view.setAccentColor(accentColor);
        view.setEpisodeNumber(episode.getSeasonEpisodeNumber().getSeason(), episode.getSeasonEpisodeNumber().getEpisode());
        view.setEpisodeDescription(episode.getOverview());
        return view;
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
