package com.ataulm.mystories.configuration;

import java.util.List;

public class Configuration {

    private final static Configuration EMPTY = new Configuration(null);

    private final Images images;

    public static Configuration empty() {
        return EMPTY;
    }

    static Configuration from(GsonConfiguration gsonConfiguration) {
        Images images = Images.from(gsonConfiguration.images);
        return new Configuration(images);
    }

    Configuration(Images images) {
        this.images = images;
    }

    static class Images {

        private final String baseUrl;
        private final String secureBaseUrl;
        private final List<String> backdropSizes;
        private final List<String> logoSizes;
        private final List<String> posterSizes;
        private final List<String> profileSizes;
        private final List<String> stillSizes;

        static Images from(GsonConfiguration.Images gsonConfiguration) {
            return new Images(gsonConfiguration.baseUrl,
                    gsonConfiguration.secureBaseUrl,
                    gsonConfiguration.backdropSizes,
                    gsonConfiguration.logoSizes,
                    gsonConfiguration.posterSizes,
                    gsonConfiguration.profileSizes,
                    gsonConfiguration.stillSizes);
        }

        Images(String baseUrl, String secureBaseUrl, List<String> backdropSizes, List<String> logoSizes, List<String> posterSizes, List<String> profileSizes, List<String> stillSizes) {
            this.baseUrl = baseUrl;
            this.secureBaseUrl = secureBaseUrl;
            this.backdropSizes = backdropSizes;
            this.logoSizes = logoSizes;
            this.posterSizes = posterSizes;
            this.profileSizes = profileSizes;
            this.stillSizes = stillSizes;
        }

    }

}
