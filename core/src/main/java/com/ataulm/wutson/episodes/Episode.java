package com.ataulm.wutson.episodes;

import com.ataulm.wutson.shows.SimpleDate;

import java.net.URI;

public class Episode {

    private final SimpleDate airDate;
    private final SeasonEpisodeNumber seasonEpisodeNumber;
    private final String name;
    private final String overview;
    private final URI stillPath;
    private final String showName;

    public Episode(SimpleDate airDate, SeasonEpisodeNumber seasonEpisodeNumber, String name, String overview, URI stillPath, String showName) {
        this.airDate = airDate;
        this.seasonEpisodeNumber = seasonEpisodeNumber;
        this.name = name;
        this.overview = overview;
        this.stillPath = stillPath;
        this.showName = showName;
    }

    public SeasonEpisodeNumber getSeasonEpisodeNumber() {
        return seasonEpisodeNumber;
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

    public String getShowName() {
        return showName;
    }

    @Override
    public String toString() {
        return Episode.class.getSimpleName() + ": [" +
                showName + " | " +
                seasonEpisodeNumber +
                airDate + "]";
    }

}
