package com.ataulm.wutson.tmdb.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GsonConfiguration {

    private static final int PROFILE_SIZE_STANDARD = 1;
    private static final int POSTER_SIZE_STANDARD = 2;

    @SerializedName("images")
    final GsonImages gsonImages;

    private GsonConfiguration(GsonImages gsonImages) {
        this.gsonImages = gsonImages;
    }

    public String getCompletePosterPath(String posterPath) {
        return gsonImages.baseUrl + gsonImages.posterSizes.get(POSTER_SIZE_STANDARD) + posterPath;
    }

    public String getCompleteProfilePath(String profilePath) {
        return gsonImages.baseUrl + gsonImages.profileSizes.get(PROFILE_SIZE_STANDARD) + profilePath;
    }

    static class GsonImages {

        @SerializedName("base_url")
        String baseUrl;

        @SerializedName("profile_sizes")
        List<String> profileSizes;

        @SerializedName("poster_sizes")
        List<String> posterSizes;

    }

}
