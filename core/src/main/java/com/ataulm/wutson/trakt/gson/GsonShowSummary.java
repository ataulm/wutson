package com.ataulm.wutson.trakt.gson;

import com.google.gson.annotations.SerializedName;

public class GsonShowSummary {

    @SerializedName("ids")
    public GsonShowIds ids;

    @SerializedName("title")
    public String title;

    @SerializedName("year")
    public int year;

    @SerializedName("images")
    public GsonImages images;

}
