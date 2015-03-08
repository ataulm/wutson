package com.ataulm.wutson.discover;

import com.ataulm.wutson.tmdb.gson.GsonGenres;

import java.util.Iterator;
import java.util.List;

public class ShowsInGenre implements Iterable<Show>, Comparable<ShowsInGenre> {

    private final GsonGenres.GsonGenre gsonGenre;
    private final List<Show> shows;

    ShowsInGenre(GsonGenres.GsonGenre gsonGenre, List<Show> shows) {
        this.gsonGenre = gsonGenre;
        this.shows = shows;
    }

    String getGsonGenre() {
        return gsonGenre.name;
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
        return getGsonGenre().compareTo(other.getGsonGenre());
    }

}
