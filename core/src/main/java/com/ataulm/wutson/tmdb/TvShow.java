package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShow {

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("poster_path")
    String posterPath;

    @SerializedName("overview")
    String overview;

    @SerializedName("seasons")
    List<Season> seasons;

    @SerializedName("credits")
    GsonCredits gsonCredits;

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

    public List<Season> getSeasons() {
        return seasons;
    }

    public GsonCredits getGsonCredits() {
        return gsonCredits;
    }

}
