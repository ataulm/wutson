package com.ataulm.wutson.tmdb.gson;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public final class GsonCredits {

    @SerializedName("cast")
    public final Cast cast;

    private GsonCredits(Cast cast) {
        this.cast = cast;
    }

    public static final class Cast extends ArrayList<Cast.Entry> {

        public static final class Entry {

            @SerializedName("character")
            public final String name;

            @SerializedName("name")
            public final String actorName;

            @SerializedName("profile_path")
            public final String profilePath;

            private Entry(String name, String actorName, String profilePath) {
                this.name = name;
                this.actorName = actorName;
                this.profilePath = profilePath;
            }

        }

    }

}
