package com.ataulm.wutson.episodes;

import java.net.URI;

public class Episode {

    private final String airDate; // TODO: should be typed
    private final int seasonNumber;
    private final int episodeNumber;
    private final String name;
    private final String overview;
    private final URI stillPath;
    private final String showName;

    public Episode(String airDate, int seasonNumber, int episodeNumber, String name, String overview, URI stillPath, String showName) {
        this.airDate = airDate;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.name = name;
        this.overview = overview;
        this.stillPath = stillPath;
        this.showName = showName;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public URI getStillPath() {
        return stillPath;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getAirDate() {
        return airDate;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public String getShowName() {
        return showName;
    }

}
