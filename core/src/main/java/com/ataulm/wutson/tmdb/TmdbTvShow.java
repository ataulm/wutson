package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TmdbTvShow {

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("seasons")
    List<TmdbSeason> tmdbSeasons;

    public String getName() {
        return name;
    }

    public List<TmdbSeason> getTmdbSeasons() {
        return tmdbSeasons;
    }

}
