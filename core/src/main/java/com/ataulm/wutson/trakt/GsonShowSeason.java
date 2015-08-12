package com.ataulm.wutson.trakt;

import com.ataulm.wutson.trakt.gson.GsonImages;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GsonShowSeason {

    @SerializedName("ids")
    public GsonSeasonIds ids;

    @SerializedName("number")
    public int number;

    @SerializedName("overview")
    public String overview;

    @SerializedName("episodes")
    public List<GsonShowEpisode> episodes;

    @SerializedName("images")
    public GsonImages images;

}
