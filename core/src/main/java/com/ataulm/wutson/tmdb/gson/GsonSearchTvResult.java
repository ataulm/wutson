package com.ataulm.wutson.tmdb.gson;

import com.google.gson.annotations.SerializedName;

public final class GsonSearchTvResult {

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

    private GsonSearchTvResult(String id, String name, String posterPath, String backdropPath, String overview) {
        this.id = id;
        this.name = name;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
    }

}
