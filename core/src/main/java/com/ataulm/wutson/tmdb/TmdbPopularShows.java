package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

import java.util.Iterator;
import java.util.List;

public class TmdbPopularShows implements Iterable<TmdbPopularShow> {

    @SerializedName("id")
    String id;

    @SerializedName("page")
    String page;

    @SerializedName("results")
    List<TmdbPopularShow> shows;

    @SerializedName("total_pages")
    int totalPages;

    @SerializedName("total_results")
    int totalResults;

    @Override
    public Iterator<TmdbPopularShow> iterator() {
        return shows.iterator();
    }

}
