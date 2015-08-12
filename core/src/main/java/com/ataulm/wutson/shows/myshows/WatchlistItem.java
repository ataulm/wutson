package com.ataulm.wutson.shows.myshows;

import com.ataulm.wutson.episodes.Episodes;

public final class WatchlistItem {

    private final String showName;
    private final Episodes episodes;

    public WatchlistItem(String showName, Episodes episodes) {
        this.showName = showName;
        this.episodes = episodes;
    }

    public String getShowName() {
        return showName;
    }

    public Episodes getEpisodes() {
        return episodes;
    }

}
