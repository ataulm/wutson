package com.ataulm.wutson.discover;

import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.tmdb.gson.GsonGenres;

import java.util.Iterator;
import java.util.List;

public class ShowsInGenre implements Iterable<ShowSummary>, Comparable<ShowsInGenre> {

    // TODO: should not be using Gson model here
    private final GsonGenres.Genre genre;
    private final List<ShowSummary> showSummaries;

    ShowsInGenre(GsonGenres.Genre genre, List<ShowSummary> showSummaries) {
        this.genre = genre;
        this.showSummaries = showSummaries;
    }

    String getGenre() {
        return genre.name;
    }

    int size() {
        return showSummaries.size();
    }

    ShowSummary get(int position) {
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
