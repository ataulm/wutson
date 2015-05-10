package com.ataulm.wutson.core.tmdb.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class GsonTvShow {

    @SerializedName("id")
    public final String id;

    @SerializedName("name")
    public final String name;

    @SerializedName("poster_path")
    public final String posterPath;

    @SerializedName("backdrop_path")
    public final String backdropPath;

    @SerializedName("overview")
    public final String overview;

    @SerializedName("seasons")
    public final List<Season> seasons;

    @SerializedName("credits")
    public final GsonCredits gsonCredits;

    private GsonTvShow(String id, String name, String posterPath, String backdropPath, String overview, List<Season> seasons, GsonCredits gsonCredits) {
        this.id = id;
        this.name = name;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.seasons = seasons;
        this.gsonCredits = gsonCredits;
    }

    public static final class Season {

        @SerializedName("id")
        public final String id;

        @SerializedName("episode_count")
        public final int episodeCount;

        @SerializedName("poster_path")
        public final String posterPath;

        @SerializedName("season_number")
        public final int seasonNumber;

        private Season(String id, int episodeCount, String posterPath, int seasonNumber) {
            this.id = id;
            this.episodeCount = episodeCount;
            this.posterPath = posterPath;
            this.seasonNumber = seasonNumber;
        }

    }

}
