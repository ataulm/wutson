package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

public class Character {

    @SerializedName("character")
    public final String name;

    @SerializedName("name")
    public final String actorName;

    @SerializedName("profile_path")
    public final String profilePath;

    public Character(String name, String actorName, String profilePath) {
        this.name = name;
        this.actorName = actorName;
        this.profilePath = profilePath;
    }

}
