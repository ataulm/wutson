package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TmdbTvShow {

    @SerializedName("name")
    String name;

    @SerializedName("id")
    String id;

    @SerializedName("seasons")
    List<Object> seasons;

    public String getName() {
        return name;
    }

    public List<Object> getSeasons() {
        return seasons;
    }

}
