package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class GsonCredits {

    @SerializedName("cast")
    public final List<GsonCastElement> gsonCastElements;

    private GsonCredits(List<GsonCastElement> gsonCastElements) {
        this.gsonCastElements = gsonCastElements;
    }

    public static final class GsonCastElement {

        @SerializedName("character")
        public final String name;

        @SerializedName("name")
        public final String actorName;

        @SerializedName("profile_path")
        public final String profilePath;

        private GsonCastElement(String name, String actorName, String profilePath) {
            this.name = name;
            this.actorName = actorName;
            this.profilePath = profilePath;
        }

    }

}
