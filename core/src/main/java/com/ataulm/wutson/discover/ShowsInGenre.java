package com.ataulm.wutson.discover;

import com.ataulm.wutson.tmdb.gson.GsonGenres;

import java.util.Iterator;
import java.util.List;

public class ShowsInGenre implements Iterable<Show>, Comparable<ShowsInGenre> {

    private final GsonGenres.GsonGenre gsonGenre;
    private final List<Show> shows;

    public ShowsInGenre(GsonGenres.GsonGenre gsonGenre, List<Show> shows) {
        this.gsonGenre = gsonGenre;
        this.shows = shows;
    }

    public String getGsonGenre() {
        return gsonGenre.name;
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

    @Override
    public int compareTo(ShowsInGenre other) {
        return getGsonGenre().compareTo(other.getGsonGenre());
    }

}
