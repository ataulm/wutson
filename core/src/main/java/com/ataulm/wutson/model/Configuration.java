package com.ataulm.wutson.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Configuration {

    private static final int POSTER_SIZE_STANDARD = 2;

    @SerializedName("images")
    Images images;

    public String getCompletePosterPath(String posterPath) {
        return images.baseUrl + images.posterSizes.get(POSTER_SIZE_STANDARD) + posterPath;
    }

    static class Images {

        @SerializedName("base_url")
        String baseUrl;

        @SerializedName("backdrop_sizes")
        List<String> backdropSizes;

        @SerializedName("poster_sizes")
        List<String> posterSizes;

    }

}
