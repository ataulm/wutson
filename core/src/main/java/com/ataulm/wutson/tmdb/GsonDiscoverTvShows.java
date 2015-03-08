package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

import java.util.Iterator;
import java.util.List;

public class GsonDiscoverTvShows implements Iterable<GsonDiscoverTvShows.Show> {

    @SerializedName("results")
    public final List<Show> shows;

    private GsonDiscoverTvShows(List<Show> shows) {
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

        public Show(String id, String name, String posterPath) {
            this.id = id;
            this.name = name;
            this.posterPath = posterPath;
        }

        @Override
        public String toString() {
            return name;
        }

    }

}
