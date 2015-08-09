package com.ataulm.wutson.seasons;

import com.ataulm.wutson.episodes.Episode;

import java.util.Iterator;
import java.util.List;

public class Season implements Iterable<com.ataulm.wutson.episodes.Episode>, Comparable<Season> {

    private final int seasonNumber;
    private final List<Episode> episodes;

    public Season(int seasonNumber, List<Episode> episodes) {
        this.seasonNumber = seasonNumber;
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
