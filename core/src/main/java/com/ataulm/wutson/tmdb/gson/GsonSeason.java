package com.ataulm.wutson.tmdb.gson;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GsonSeason {

    @SerializedName("id")
    public final String id;

    @SerializedName("air_date")
    public final String airDate;

    @SerializedName("season_number")
    public final int seasonNumber;

    @SerializedName("overview")
    public final String overview;

    @SerializedName("poster_path")
    public final String posterPath;

    @SerializedName("episodes")
    public final Episodes episodes;

    private GsonSeason(String id, String airDate, int seasonNumber, String overview, String posterPath, Episodes episodes) {
        this.id = id;
        this.airDate = airDate;
        this.seasonNumber = seasonNumber;
        this.overview = overview;
        this.posterPath = posterPath;
        this.episodes = episodes;
    }

    public static final class Episodes extends ArrayList<Episodes.Episode> {

        public static final class Episode {

            @SerializedName("air_date")
            public final String airDate;

            @SerializedName("episode_number")
            public final int episodeNumber;

            @SerializedName("name")
            public final String name;

            @SerializedName("overview")
            public final String overview;

            @SerializedName("still_path")
            public final String stillPath;

            private Episode(String airDate, int episodeNumber, String name, String overview, String stillPath) {
                this.airDate = airDate;
                this.episodeNumber = episodeNumber;
                this.name = name;
                this.overview = overview;
                this.stillPath = stillPath;
            }

        }

    }

}
