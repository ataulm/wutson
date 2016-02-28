package com.ataulm.wutson.trakt;

import com.google.gson.annotations.SerializedName;

public class GsonSearchTvResult {

    @SerializedName("score")
    public String score;

    @SerializedName("show")
    public Show show;

    public static class Show {

        @SerializedName("ids")
        public GsonEpisodeIds ids;

        @SerializedName("images")
        public Show.Images images;

        @SerializedName("title")
        public String title;

        @SerializedName("overview")
        public String overview;

        public static class Images {

            @SerializedName("poster")
            public GsonImage poster;

        }

    }

}
