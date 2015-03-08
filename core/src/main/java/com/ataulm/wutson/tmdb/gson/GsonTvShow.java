package com.ataulm.wutson.tmdb.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class GsonTvShow {

    @SerializedName("name")
    public final String name;

    @SerializedName("poster_path")
    public final String posterPath;

    @SerializedName("overview")
    public final String overview;

    @SerializedName("seasons")
    public final List<GsonSeason> gsonSeasons;

    @SerializedName("credits")
    public final GsonCredits gsonCredits;

    private GsonTvShow(String name, String posterPath, String overview, List<GsonSeason> gsonSeasons, GsonCredits gsonCredits) {
        this.name = name;
        this.posterPath = posterPath;
        this.overview = overview;
        this.gsonSeasons = gsonSeasons;
        this.gsonCredits = gsonCredits;
    }

    public static final class GsonSeason {

        @SerializedName("id")
        public final String id;

        @SerializedName("episode_count")
        public final int episodeCount;

        @SerializedName("poster_path")
        public final String posterPath;

        @SerializedName("season_number")
        public final int seasonNumber;

        private GsonSeason(String id, int episodeCount, String posterPath, int seasonNumber) {
            this.id = id;
            this.episodeCount = episodeCount;
            this.posterPath = posterPath;
            this.seasonNumber = seasonNumber;
        }

    }

}
