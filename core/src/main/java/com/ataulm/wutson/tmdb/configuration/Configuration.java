package com.ataulm.wutson.tmdb.configuration;

import java.util.List;

/**
 * Get the system wide configuration information.
 *
 * Some elements of the API require some knowledge of this configuration data.
 * The purpose of this is to try and keep the actual API responses as light as possible.
 * It is recommended you cache this data within your application and check for updates every few days.
 *
 * This method currently holds the data relevant to building image URLs as well as the change key map.
 *
 * To build an image URL, you will need 3 pieces of data. The base_url, size and file_path.
 * Simply combine them all and you will have a fully qualified URL.
 *
 * Here’s an example URL: http://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg
 */
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

    public String getSecureImageBaseUrl() {
        return images.secureBaseUrl;
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
