package com.ataulm.wutson.discover;

import com.ataulm.wutson.model.DiscoverTvShows;
import com.ataulm.wutson.model.Genre;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShowsInGenre implements Iterable<Show> {

    private final Genre genre;
    private final List<Show> shows;

    public static ShowsInGenre from(Genre genre, DiscoverTvShows discoverTvShows) {
        List<Show> shows = new ArrayList<>();
        for (DiscoverTvShows.Show discoverTvShow : discoverTvShows) {
            Show show = new Show(discoverTvShow.id, discoverTvShow.name);
            shows.add(show);
        }
        return new ShowsInGenre(genre, shows);
    }

    ShowsInGenre(Genre genre, List<Show> shows) {
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
