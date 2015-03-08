package com.ataulm.wutson.seasons;

import java.util.Iterator;
import java.util.List;

public class Seasons implements Iterable<Season> {

    private final List<Season> seasons;

    Seasons(List<Season> seasons) {
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

}
