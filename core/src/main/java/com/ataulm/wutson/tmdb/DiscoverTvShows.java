package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

import java.util.Iterator;
import java.util.List;

public class DiscoverTvShows implements Iterable<DiscoverTvShows.Show> {

    @SerializedName("id")
    String id;

    @SerializedName("page")
    String page;

    @SerializedName("results")
    List<Show> shows;

    @SerializedName("total_pages")
    int totalPages;

    @SerializedName("total_results")
    int totalResults;

    @Override
    public Iterator<Show> iterator() {
        return shows.iterator();
    }

    public int size() {
        return shows.size();
    }

    public class Show {

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
