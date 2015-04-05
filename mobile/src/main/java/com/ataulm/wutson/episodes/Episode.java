package com.ataulm.wutson.episodes;

import java.net.URI;

public class Episode {

    private final String airDate; // TODO: should be typed
    private final int episodeNumber;
    private final String name;
    private final String overview;
    private final URI stillPath;

    public Episode(String airDate, int episodeNumber, String name, String overview, URI stillPath) {
        this.airDate = airDate;
        this.episodeNumber = episodeNumber;
        this.name = name;
        this.overview = overview;
        this.stillPath = stillPath;
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

    public String getAirDate() {
        return airDate;
    }
}
