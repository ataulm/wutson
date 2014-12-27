package com.ataulm.wutson.trackedshows;

import com.ataulm.wutson.tmdb.discovertv.DiscoverTv;

public class TrackedShow {

    private final String id;
    private final String showName;
    private final String nextEpisodeName;
    private final String posterUrl;

    public static TrackedShow from(DiscoverTv.Show discoverTvShow, String nextEpisodeName) {
        String id = String.valueOf(discoverTvShow.getId());
        String showName = discoverTvShow.getName();
        String posterUrl = discoverTvShow.getPosterPath();

        return new TrackedShow(id, showName, nextEpisodeName, posterUrl);
    }

    TrackedShow(String id, String showName, String nextEpisodeName, String posterUrl) {
        this.id = id;
        this.showName = showName;
        this.nextEpisodeName = nextEpisodeName;
        this.posterUrl = posterUrl;
    }

    public String getId() {
        return id;
    }

    public String getShowName() {
        return showName;
    }

    public String getNextEpisodeName() {
        return nextEpisodeName;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

}
