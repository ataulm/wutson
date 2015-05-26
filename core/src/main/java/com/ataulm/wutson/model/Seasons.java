package com.ataulm.wutson.model;

import java.util.Iterator;
import java.util.List;

public class Seasons implements Iterable<Season> {

    private final Show show;
    private final List<Season> seasons;

    public Seasons(Show show, List<Season> seasons) {
        this.show = show;
        this.seasons = seasons;
    }

    @Override
    public Iterator<Season> iterator() {
        return seasons.iterator();
    }

    public Season get(int position) {
        return seasons.get(position);
    }

    public int size() {
        return seasons.size();
    }

    public String getShowId() {
        return show.getId();
    }

    public String getShowName() {
        return show.getName();
    }
}
