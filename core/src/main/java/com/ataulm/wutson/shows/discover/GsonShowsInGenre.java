package com.ataulm.wutson.shows.discover;

import com.ataulm.wutson.tmdb.gson.GsonDiscoverTv;
import com.ataulm.wutson.tmdb.gson.GsonGenres;

class GsonShowsInGenre {

    private final GsonGenres.Genre genre;
    private final GsonDiscoverTv shows;

    GsonShowsInGenre(GsonGenres.Genre genre, GsonDiscoverTv shows) {
        this.genre = genre;
        this.shows = shows;
    }

    public GsonGenres.Genre getGenre() {
        return genre;
    }

    public GsonDiscoverTv getShows() {
        return shows;
    }

}
