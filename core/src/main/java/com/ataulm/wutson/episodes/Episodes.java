package com.ataulm.wutson.episodes;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Episodes implements Iterable<Episode> {

    private final List<Episode> episodes;

    public Episodes(List<Episode> episodes) {
        this.episodes = Collections.unmodifiableList(episodes);
    }

    public int size() {
        return episodes.size();
    }

    public Episode get(int location) {
        return episodes.get(location);
    }

    @Override
    public Iterator<Episode> iterator() {
        return episodes.iterator();
    }

}
