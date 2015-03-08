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

    String getName() {
        return name;
    }

    URI getPosterUri() {
        return posterUri;
    }

    String getOverview() {
        return overview;
    }

    Cast getCast() {
        return cast;
    }

    List<Season> getSeasons() {
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

        String getId() {
            return id;
        }

        int getSeasonNumber() {
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
