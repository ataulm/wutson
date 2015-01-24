package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

public class TmdbSeason {

    @SerializedName("season_number")
    int seasonNumber;

    @SerializedName("episode_count")
    int episodeCount;

    @Override
    public String toString() {
        return "Season " + seasonNumber + " has " + episodeCount + " episodes.";
    }

}
