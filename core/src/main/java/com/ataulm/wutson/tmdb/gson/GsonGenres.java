package com.ataulm.wutson.tmdb.gson;

import com.google.gson.annotations.SerializedName;

import java.util.Iterator;
import java.util.List;

public final class GsonGenres implements Iterable<GsonGenres.Genre> {

    @SerializedName("genres")
    final List<Genre> genres;

    private GsonGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public Iterator<Genre> iterator() {
        return genres.iterator();
    }

    public static final class Genre {

        @SerializedName("id")
        public final String id;

        @SerializedName("name")
        public final String name;

        private Genre(String id, String name) {
            this.id = id;
            this.name = name;
        }

    }
}
