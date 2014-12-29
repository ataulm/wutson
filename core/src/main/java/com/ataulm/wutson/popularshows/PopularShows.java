package com.ataulm.wutson.popularshows;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PopularShows implements Iterable<PopularShow> {

    private final List<PopularShow> popularShows;

    public PopularShows(List<PopularShow> popularShows) {
        this.popularShows = Collections.unmodifiableList(popularShows);
    }

    public int size() {
        return popularShows.size();
    }

    public PopularShow get(int position) {
        return popularShows.get(position);
    }

    @Override
    public Iterator<PopularShow> iterator() {
        return popularShows.iterator();
    }

}
