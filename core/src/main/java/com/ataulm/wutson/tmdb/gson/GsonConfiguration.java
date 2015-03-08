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

    private static class GsonImages {

        @SerializedName("base_url")
        final String baseUrl;

        @SerializedName("profile_sizes")
        final List<String> profileSizes;

        @SerializedName("poster_sizes")
        final List<String> posterSizes;

        private GsonImages(String baseUrl, List<String> profileSizes, List<String> posterSizes) {
            this.baseUrl = baseUrl;
            this.profileSizes = profileSizes;
            this.posterSizes = posterSizes;
        }

    }

}
