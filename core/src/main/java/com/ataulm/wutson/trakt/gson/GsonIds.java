package com.ataulm.wutson.trakt.gson;

import com.google.gson.annotations.SerializedName;

public class GsonIds {

    @SerializedName("imdb")
    public final String imdb;

    @SerializedName("trakt")
    public final String trakt;

    private GsonIds(String imdb, String trakt) {
        this.imdb = imdb;
        this.trakt = trakt;
    }

}
