package com.ataulm.wutson.trackedshows;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TrackedShows implements Iterable<TrackedShow> {

    private final List<TrackedShow> trackedShows;

    public TrackedShows(List<TrackedShow> trackedShows) {
        this.trackedShows = Collections.unmodifiableList(trackedShows);
    }

    public int size() {
        return trackedShows.size();
    }

    public TrackedShow get(int position) {
        return trackedShows.get(position);
    }

    @Override
    public Iterator<TrackedShow> iterator() {
        return trackedShows.iterator();
    }

}
