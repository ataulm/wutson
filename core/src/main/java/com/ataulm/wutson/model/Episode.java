package com.ataulm.wutson.model;

import java.net.URI;

public class Episode {

    private final SimpleDate airDate;
    private final int seasonNumber;
    private final int episodeNumber;
    private final String name;
    private final String overview;
    private final URI stillPath;
    private final String showName;

    public Episode(SimpleDate airDate, int seasonNumber, int episodeNumber, String name, String overview, URI stillPath, String showName) {
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

    public SimpleDate getAirDate() {
        return airDate;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public String getShowName() {
        return showName;
    }

}
