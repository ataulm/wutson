package com.ataulm.wutson.discover;

import com.ataulm.wutson.tmdb.gson.GsonGenres;

import java.util.Iterator;
import java.util.List;

public class ShowsInGenre implements Iterable<Show>, Comparable<ShowsInGenre> {

    // TODO: should not be using Gson model here
    private final GsonGenres.Genre genre;
    private final List<Show> shows;

    ShowsInGenre(GsonGenres.Genre genre, List<Show> shows) {
        this.genre = genre;
        this.shows = shows;
    }

    String getGenre() {
        return genre.name;
    }

    int size() {
        return shows.size();
    }

    Show get(int position) {
        return shows.get(position);
    }

    @Override
    public Iterator<Show> iterator() {
        return shows.iterator();
    }

    @Override
    public int compareTo(ShowsInGenre other) {
        return getGenre().compareTo(other.getGenre());
    }

}
