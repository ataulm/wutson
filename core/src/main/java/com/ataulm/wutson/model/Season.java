package com.ataulm.wutson.model;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

public class Season implements Iterable<Episode>, Comparable<Season> {

    private final SimpleDate airDate;
    private final int seasonNumber;
    private final String overview;
    private final URI posterPath;
    private final List<Episode> episodes;

    public Season(SimpleDate airDate, int seasonNumber, String overview, URI posterPath, List<Episode> episodes) {
        this.airDate = airDate;
        this.seasonNumber = seasonNumber;
        this.overview = overview;
        this.posterPath = posterPath;
        this.episodes = episodes;
    }

    @Override
    public Iterator<Episode> iterator() {
        return episodes.iterator();
    }

    public int size() {
        return episodes.size();
    }

    public Episode get(int position) {
        return episodes.get(position);
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    @Override
    public int compareTo(Season o) {
        return getSeasonNumber() - o.getSeasonNumber();
    }

}
