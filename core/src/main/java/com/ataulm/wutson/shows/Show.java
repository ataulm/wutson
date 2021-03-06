package com.ataulm.wutson.shows;

import java.net.URI;
import java.util.List;

public class Show {

    private final ShowId id;
    private final String name;
    private final String overview;
    private final URI posterUri;
    private final URI backdropUri;
    private final Cast cast;
    private final List<SeasonSummary> seasonSummaries;

    public Show(ShowId id, String name, String overview, URI posterUri, URI backdropUri, Cast cast, List<SeasonSummary> seasonSummaries) {
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.posterUri = posterUri;
        this.backdropUri = backdropUri;
        this.cast = cast;
        this.seasonSummaries = seasonSummaries;
    }

    public ShowId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    URI getPosterUri() {
        return posterUri;
    }

    public URI getBackdropUri() {
        return backdropUri;
    }

    public String getOverview() {
        return overview;
    }

    public Cast getCast() {
        return cast;
    }

    public List<SeasonSummary> getSeasonSummaries() {
        return seasonSummaries;
    }

    public static class SeasonSummary {

        private final String id;
        private final ShowId showId;
        private final String showName;
        private final int seasonNumber;
        private final int episodeCount;
        private final URI posterPath;

        public SeasonSummary(String id, ShowId showId, String showName, int seasonNumber, int episodeCount, URI posterPath) {
            this.id = id;
            this.showId = showId;
            this.showName = showName;
            this.seasonNumber = seasonNumber;
            this.episodeCount = episodeCount;
            this.posterPath = posterPath;
        }

        public String getId() {
            return id;
        }

        public ShowId getShowId() {
            return showId;
        }

        public String getShowName() {
            return showName;
        }

        public int getSeasonNumber() {
            return seasonNumber;
        }

        public int getEpisodeCount() {
            return episodeCount;
        }

        public URI getPosterPath() {
            return posterPath;
        }

    }

}
