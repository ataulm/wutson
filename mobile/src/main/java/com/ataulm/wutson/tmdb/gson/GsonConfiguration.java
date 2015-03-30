package com.ataulm.wutson.tmdb.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GsonConfiguration {

    @SerializedName("images")
    public final Images images;

    private GsonConfiguration(Images images) {
        this.images = images;
    }

    public static class Images {

        @SerializedName("base_url")
        public final String baseUrl;

        @SerializedName("profile_sizes")
        public final List<String> profileSizes;

        @SerializedName("poster_sizes")
        public final List<String> posterSizes;

        @SerializedName("backdrop_sizes")
        public final List<String> backdropSizes;

        @SerializedName("still_sizes")
        public final List<String> stillSizes;

        private Images(String baseUrl, List<String> profileSizes, List<String> posterSizes, List<String> backdropSizes, List<String> stillSizes) {
            this.baseUrl = baseUrl;
            this.profileSizes = profileSizes;
            this.posterSizes = posterSizes;
            this.backdropSizes = backdropSizes;
            this.stillSizes = stillSizes;
        }

    }

}
