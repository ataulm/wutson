package com.ataulm.wutson.core.model;

import java.net.URI;
import java.util.List;

public class TmdbConfiguration {

    private static final int PROFILE_SIZE_STANDARD = 1;
    private static final int POSTER_SIZE_STANDARD = 2;
    private static final int BACKDROP_SIZE_STANDARD = 2;
    private static final int STILL_SIZE_STANDARD = 2;

    private final String baseUrl;
    private final List<String> profileImageSizes;
    private final List<String> posterImageSizes;
    private final List<String> backdropImageSizes;
    private final List<String> stillImageSizes;

    public TmdbConfiguration(String baseUrl, List<String> profileImageSizes, List<String> posterImageSizes, List<String> backdropImageSizes, List<String> stillImageSizes) {
        this.baseUrl = baseUrl;
        this.profileImageSizes = profileImageSizes;
        this.posterImageSizes = posterImageSizes;
        this.backdropImageSizes = backdropImageSizes;
        this.stillImageSizes = stillImageSizes;
    }

    public URI completeStill(String partialPath) {
        return URI.create(baseUrl + stillImageSizes.get(STILL_SIZE_STANDARD) + partialPath);
    }

    public URI completePoster(String partialPath) {
        return URI.create(baseUrl + posterImageSizes.get(POSTER_SIZE_STANDARD) + partialPath);
    }

    public URI completeBackdrop(String partialPath) {
        return URI.create(baseUrl + backdropImageSizes.get(BACKDROP_SIZE_STANDARD) + partialPath);
    }

    public URI completeProfile(String partialPath) {
        return URI.create(baseUrl + profileImageSizes.get(PROFILE_SIZE_STANDARD) + partialPath);
    }

}
