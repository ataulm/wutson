package com.ataulm.wutson.trakt.gson;

import com.google.gson.annotations.SerializedName;

public class GsonTrendingShow {

    @SerializedName("show")
    public final GsonShowSummary show;

    @SerializedName("watching")
    public final int watching;

    private GsonTrendingShow(GsonShowSummary show, int watching) {
        this.show = show;
        this.watching = watching;
    }

}
