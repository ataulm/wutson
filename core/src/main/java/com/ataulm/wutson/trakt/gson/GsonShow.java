package com.ataulm.wutson.trakt.gson;

import com.google.gson.annotations.SerializedName;

public class GsonShow {

    @SerializedName("ids")
    public final GsonIds ids;

    @SerializedName("title")
    public final String title;

    @SerializedName("year")
    public final int year;

    @SerializedName("images")
    public final GsonImages images;

    private GsonShow(GsonIds ids, String title, int year, GsonImages images) {
        this.ids = ids;
        this.title = title;
        this.year = year;
        this.images = images;
    }

}
