package com.ataulm.wutson.show;

import java.net.URI;
import java.util.List;

public class Show {

    private final String name;
    private final String overview;
    private final URI posterUri;
    private final Cast cast;
    private final List<Season> seasons;

    public Show(String name, String overview, URI posterUri, Cast cast, List<Season> seasons) {
        this.name = name;
        this.overview = overview;
        this.posterUri = posterUri;
        this.cast = cast;
        this.seasons = seasons;
    }

    public String getName() {
        return name;
    }

    public URI getPosterUri() {
        return posterUri;
    }

    public String getOverview() {
        return overview;
    }

    public Cast getCast() {
        return cast;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public static class Season {

        private final String id;
        private final int seasonNumber;
        private final int episodeCount;
        private final URI posterPath;

        public Season(String id, int seasonNumber, int episodeCount, URI posterPath) {
            this.id = id;
            this.seasonNumber = seasonNumber;
            this.episodeCount = episodeCount;
            this.posterPath = posterPath;
        }

        public String getId() {
            return id;
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
