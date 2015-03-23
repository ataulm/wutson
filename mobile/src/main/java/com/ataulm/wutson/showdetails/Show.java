package com.ataulm.wutson.showdetails;

import java.net.URI;
import java.util.List;

public class Show {

    private final String id;
    private final String name;
    private final String overview;
    private final URI posterUri;
    private final URI backdropUri;
    private final Cast cast;
    private final List<Season> seasons;

    Show(String id, String name, String overview, URI posterUri, URI backdropUri, Cast cast, List<Season> seasons) {
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.posterUri = posterUri;
        this.backdropUri = backdropUri;
        this.cast = cast;
        this.seasons = seasons;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    URI getPosterUri() {
        return posterUri;
    }

    URI getBackdropUri() {
        return backdropUri;
    }

    String getOverview() {
        return overview;
    }

    Cast getCast() {
        return cast;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public static class Season {

        private final String id;
        private final String showId;
        private final int seasonNumber;
        private final int episodeCount;
        private final URI posterPath;

        Season(String id, String showId, int seasonNumber, int episodeCount, URI posterPath) {
            this.id = id;
            this.showId = showId;
            this.seasonNumber = seasonNumber;
            this.episodeCount = episodeCount;
            this.posterPath = posterPath;
        }

        String getId() {
            return id;
        }

        public String getShowId() {
            return showId;
        }

        public int getSeasonNumber() {
            return seasonNumber;
        }

        int getEpisodeCount() {
            return episodeCount;
        }

        URI getPosterPath() {
            return posterPath;
        }

    }

}
