package com.ataulm.wutson.trakt.gson;

import com.google.gson.annotations.SerializedName;

public class GsonPoster {

    @SerializedName("full")
    public final String full;

    @SerializedName("full")
    public final String medium;

    @SerializedName("thumb")
    public final String thumb;

    private GsonPoster(String full, String medium, String thumb) {
        this.full = full;
        this.medium = medium;
        this.thumb = thumb;
    }

}
