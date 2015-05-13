package com.ataulm.wutson.tmdb.gson;

import com.google.gson.annotations.SerializedName;

import java.util.Iterator;
import java.util.List;

public class GsonDiscoverTv implements Iterable<GsonDiscoverTv.Show> {

    @SerializedName("results")
    public final List<Show> shows;

    private GsonDiscoverTv(List<Show> shows) {
        this.shows = shows;
    }

    @Override
    public Iterator<Show> iterator() {
        return shows.iterator();
    }

    public static class Show {

        @SerializedName("id")
        public final String id;

        @SerializedName("name")
        public final String name;

        @SerializedName("poster_path")
        public final String posterPath;

        @SerializedName("backdrop_path")
        public final String backdropPath;

        private Show(String id, String name, String posterPath, String backdropPath) {
            this.id = id;
            this.name = name;
            this.posterPath = posterPath;
            this.backdropPath = backdropPath;
        }

    }

}
