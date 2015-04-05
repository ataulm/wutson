package com.ataulm.wutson.episodes;

import java.util.Iterator;
import java.util.List;

public class Episodes implements Iterable<Episode> {

    private final String showName;
    private final List<Episode> episodes;

    public Episodes(String showName, List<Episode> episodes) {
        this.showName = showName;
        this.episodes = episodes;
    }

    public String getShowName() {
        return showName;
    }

    @Override
    public Iterator<Episode> iterator() {
        return episodes.iterator();
    }

}
