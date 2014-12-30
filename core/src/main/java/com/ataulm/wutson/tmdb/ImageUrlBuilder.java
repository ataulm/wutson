package com.ataulm.wutson.tmdb;

import com.ataulm.wutson.tmdb.configuration.Configuration;
import com.ataulm.wutson.tmdb.discovertv.DiscoverTv;

public class ImageUrlBuilder {

    // TODO: this should be one of the sizes from the Configuration object
    private static final String IMAGE_SIZE = "w500";

    private final Configuration configuration;

    public ImageUrlBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public String posterPathFor(DiscoverTv.Show discoverTvShow) {
        String secureImageBaseUrl = configuration.getSecureImageBaseUrl();
        return secureImageBaseUrl + IMAGE_SIZE + discoverTvShow.getPosterPath();
    }

}
