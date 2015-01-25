package com.ataulm.wutson.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Configuration {

    @SerializedName("images")
    Images images;

    static class Images {

        @SerializedName("base_url")
        String baseUrl;

        @SerializedName("backdrop_sizes")
        List<String> backdropSizes;

        @SerializedName("poster_sizes")
        List<String> posterSizes;

    }

}
