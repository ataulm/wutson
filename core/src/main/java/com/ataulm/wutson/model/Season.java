package com.ataulm.wutson.model;

import com.google.gson.annotations.SerializedName;

public class Season {

    @SerializedName("id")
    String id;

    @SerializedName("episode_count")
    int size;

    @SerializedName("poster_path")
    String posterPath;

    @SerializedName("season_number")
    int seasonNumber;

}
