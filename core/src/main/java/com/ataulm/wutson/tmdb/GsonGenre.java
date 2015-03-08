package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

public class GsonGenre {

    @SerializedName("id")
    public final String id;

    @SerializedName("name")
    public final String name;

    private GsonGenre(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
