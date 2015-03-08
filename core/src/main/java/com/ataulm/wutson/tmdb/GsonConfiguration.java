package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GsonConfiguration {

    private static final int PROFILE_SIZE_STANDARD = 1;
    private static final int POSTER_SIZE_STANDARD = 2;

    @SerializedName("images")
    final Images images;

    private GsonConfiguration(Images images) {
        this.images = images;
    }

    public String getCompletePosterPath(String posterPath) {
        return images.baseUrl + images.posterSizes.get(POSTER_SIZE_STANDARD) + posterPath;
    }

    public String getCompleteProfilePath(String profilePath) {
        return images.baseUrl + images.profileSizes.get(PROFILE_SIZE_STANDARD) + profilePath;
    }

    static class Images {

        @SerializedName("base_url")
        String baseUrl;

        @SerializedName("profile_sizes")
        List<String> profileSizes;

        @SerializedName("poster_sizes")
        List<String> posterSizes;

    }

}
