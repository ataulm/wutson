package com.ataulm.mystories.tmdb.configuration;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class GsonConfiguration {

    @SerializedName("images")
    Images images;

    @SerializedName("change_keys")
    List<String> changeKeys;

    static class Images {

        @SerializedName("base_url")
        String baseUrl;

        @SerializedName("secure_base_url")
        String secureBaseUrl;

        @SerializedName("backdrop_sizes")
        List<String> backdropSizes;

        @SerializedName("logo_sizes")
        List<String> logoSizes;

        @SerializedName("poster_sizes")
        List<String> posterSizes;

        @SerializedName("profile_sizes")
        List<String> profileSizes;

        @SerializedName("still_sizes")
        List<String> stillSizes;

    }

}
