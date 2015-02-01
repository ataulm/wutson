package com.ataulm.wutson.model;

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

    public Show get(int position) {
        return shows.get(position);
    }

    public class Show {

        @SerializedName("name")
        String name;

        @Override
        public String toString() {
            return name;
        }

    }

}
