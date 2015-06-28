package com.ataulm.wutson.shows.discover;

import com.ataulm.wutson.shows.Genre;
import com.ataulm.wutson.shows.ShowSummary;

import java.util.Iterator;
import java.util.List;

public class ShowsInGenre implements Iterable<ShowSummary>, Comparable<ShowsInGenre> {

    private final Genre genre;
    private final List<ShowSummary> showSummaries;

    public ShowsInGenre(Genre genre, List<ShowSummary> showSummaries) {
        this.genre = genre;
        this.showSummaries = showSummaries;
    }

    public Genre getGenre() {
        return genre;
    }

    public int size() {
        return showSummaries.size();
    }

    public ShowSummary get(int position) {
        return showSummaries.get(position);
    }

    @Override
    public Iterator<ShowSummary> iterator() {
        return showSummaries.iterator();
    }

    @Override
    public int compareTo(ShowsInGenre other) {
        return getGenre().compareTo(other.getGenre());
    }

}
