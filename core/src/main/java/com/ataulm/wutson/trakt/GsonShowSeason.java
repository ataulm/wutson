package com.ataulm.wutson.trakt;

import com.ataulm.wutson.trakt.gson.GsonImages;
import com.google.gson.annotations.SerializedName;

public class GsonShowSeason {

    @SerializedName("ids")
    public GsonSeasonIds ids;

    @SerializedName("number")
    public int number;

    @SerializedName("overview")
    public String overview;

    @SerializedName("episode_count")
    public int episodeCount;

    @SerializedName("images")
    public GsonImages images;

}
