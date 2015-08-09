package com.ataulm.wutson.trakt;

import com.google.gson.annotations.SerializedName;

public class GsonShowEpisode {

    @SerializedName("ids")
    public GsonEpisodeIds ids;

    @SerializedName("title")
    public String title;

    @SerializedName("number")
    public int number;

    @SerializedName("season")
    public int season;

    @SerializedName("first_aired")
    public String firstAiredDate;

    @SerializedName("overview")
    public String overview;

    @SerializedName("images")
    public Images images;

    public static class Images {

        @SerializedName("screenshot")
        public Images.Screenshot screenshot;

        public static class Screenshot {

            @SerializedName("full")
            public String full;

            @SerializedName("medium")
            public String medium;

            @SerializedName("thumb")
            public String thumb;

        }

    }

}
