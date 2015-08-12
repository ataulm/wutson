package com.ataulm.wutson.trakt;

import com.ataulm.wutson.trakt.gson.GsonImages;
import com.ataulm.wutson.trakt.gson.GsonShowIds;
import com.google.gson.annotations.SerializedName;

public class GsonShowDetails {

    @SerializedName("ids")
    public GsonShowIds ids;

    @SerializedName("title")
    public String title;

    @SerializedName("year")
    public int year;

    @SerializedName("images")
    public GsonImages images;

    @SerializedName("overview")
    public String overview;

}
