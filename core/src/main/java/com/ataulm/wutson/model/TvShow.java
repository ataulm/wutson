package com.ataulm.wutson.model;

import com.google.gson.annotations.SerializedName;

public class TvShow {

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("poster_path")
    String posterPath;

    @SerializedName("overview")
    String overview;

    private Configuration configuration;

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getName() {
        return name;
    }

    public String getPosterPath() {
        return configuration.getCompletePosterPath(posterPath);
    }

    public String getOverview() {
        return overview;
    }

}
