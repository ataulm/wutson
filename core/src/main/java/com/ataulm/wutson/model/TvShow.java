package com.ataulm.wutson.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShow {

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("seasons")
    List<Season> seasons;

    private Configuration configuration;

    public String getName() {
        return name;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String foo() {
        return configuration.images.baseUrl;
    }

    public class Season {

        @SerializedName("season_number")
        int seasonNumber;

        @SerializedName("episode_count")
        int episodeCount;

        @Override
        public String toString() {
            return "Season " + seasonNumber + " has " + episodeCount + " episodes.";
        }

    }

}
