package com.ataulm.wutson.tmdb.gson;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public final class GsonCredits {

    @SerializedName("cast")
    public final GsonCast gsonCast;

    private GsonCredits(GsonCast gsonCast) {
        this.gsonCast = gsonCast;
    }

    public static final class GsonCast extends ArrayList<GsonCast.GsonCastElement> {

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

}
