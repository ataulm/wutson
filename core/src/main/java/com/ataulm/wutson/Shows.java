package com.ataulm.wutson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Shows {

    private static final Shows EMPTY_SHOWS = new Shows(Collections.<Show>emptyList());

    private final List<Show> shows;

    public static Shows from(List<Show> shows) {
        List<Show> unmodifiableShows = Collections.unmodifiableList(shows);
        return new Shows(unmodifiableShows);
    }

    public static Shows empty() {
        return EMPTY_SHOWS;
    }

    Shows(List<Show> shows) {
        this.shows = shows;
    }

    public Show get(int position) {
        return shows.get(position);
    }

    public int size() {
        return shows.size();
    }

    static class Builder {

        private final List<Show> shows;

        public Builder() {
            this.shows = new ArrayList<>();
        }

        public Builder add(Show show) {
            shows.add(show);
            return this;
        }

        public Shows toShows() {
            return Shows.from(shows);
        }

    }

}
