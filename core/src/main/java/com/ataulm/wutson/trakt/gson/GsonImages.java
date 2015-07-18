package com.ataulm.wutson.trakt.gson;

import com.google.gson.annotations.SerializedName;

public class GsonImages {

    @SerializedName("poster")
    public final GsonPoster poster;

    private GsonImages(GsonPoster poster) {
        this.poster = poster;
    }

}
