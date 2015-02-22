package com.ataulm.wutson.discover;

import com.ataulm.wutson.tmdb.Genre;

import java.util.Iterator;
import java.util.List;

public class ShowsInGenre implements Iterable<Show> {

    private final Genre genre;
    private final List<Show> shows;

    public ShowsInGenre(Genre genre, List<Show> shows) {
        this.genre = genre;
        this.shows = shows;
    }

    public String getGenre() {
        return genre.getName();
    }

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

}
