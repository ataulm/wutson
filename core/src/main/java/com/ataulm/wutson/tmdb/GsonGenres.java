package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

import java.util.Iterator;
import java.util.List;

public final class GsonGenres implements Iterable<GsonGenres.GsonGenre> {

    @SerializedName("genres")
    public final List<GsonGenre> gsonGenres;

    private GsonGenres(List<GsonGenre> gsonGenres) {
        this.gsonGenres = gsonGenres;
    }

    @Override
    public Iterator<GsonGenre> iterator() {
        return gsonGenres.iterator();
    }

    public static final class GsonGenre {

        @SerializedName("id")
        public final String id;

        @SerializedName("name")
        public final String name;

        private GsonGenre(String id, String name) {
            this.id = id;
            this.name = name;
        }

    }
}
