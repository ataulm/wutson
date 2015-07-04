package com.ataulm.wutson.shows.discover;

import com.ataulm.wutson.tmdb.gson.GsonDiscoverTv;
import com.ataulm.wutson.tmdb.gson.GsonGenres;

import java.util.List;

class GsonDiscoverTvShows {
    private final GsonGenres.Genre genre;
    private final List<GsonDiscoverTv> showSummaries;

    GsonDiscoverTvShows(GsonGenres.Genre genre, List<GsonDiscoverTv> showSummaries) {
        this.genre = genre;
        this.showSummaries = showSummaries;
    }
}
