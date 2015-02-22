package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

public class Season {

    @SerializedName("id")
    public final String id;

    @SerializedName("episode_count")
    public final int episodeCount;

    @SerializedName("poster_path")
    public final String posterPath;

    @SerializedName("season_number")
    public final int seasonNumber;

    public Season(String id, int episodeCount, String posterPath, int seasonNumber) {
        this.id = id;
        this.episodeCount = episodeCount;
        this.posterPath = posterPath;
        this.seasonNumber = seasonNumber;
    }

}
