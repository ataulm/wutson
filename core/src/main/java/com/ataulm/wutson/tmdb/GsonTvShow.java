package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GsonTvShow {

    @SerializedName("name")
    public final String name;

    @SerializedName("poster_path")
    public final String posterPath;

    @SerializedName("overview")
    public final String overview;

    @SerializedName("seasons")
    public final List<Season> seasons;

    @SerializedName("credits")
    public final GsonCredits gsonCredits;

    private GsonTvShow(String name, String posterPath, String overview, List<Season> seasons, GsonCredits gsonCredits) {
        this.name = name;
        this.posterPath = posterPath;
        this.overview = overview;
        this.seasons = seasons;
        this.gsonCredits = gsonCredits;
    }

}
