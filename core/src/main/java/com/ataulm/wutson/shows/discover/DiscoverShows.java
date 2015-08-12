package com.ataulm.wutson.shows.discover;

import com.ataulm.wutson.shows.ShowSummaries;

import java.util.Arrays;
import java.util.List;

public class DiscoverShows {

    private final List<ShowSummaries> shows;

    public DiscoverShows(ShowSummaries trendingShows, ShowSummaries popularShows) {
        this.shows = Arrays.asList(trendingShows, popularShows);
    }

    public ShowSummaries getShowSummaries(int position) {
        return shows.get(position);
    }

    public int size() {
        return shows.size();
    }

    public CharSequence getTitle(int position) {
        if (position == 0) {
            return "Trending";
        } else {
            return "Popular";
        }
    }

}
