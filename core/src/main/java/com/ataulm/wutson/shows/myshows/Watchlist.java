package com.ataulm.wutson.shows.myshows;

import java.util.List;

public class Watchlist {

    private final List<WatchlistItem> items;

    public Watchlist(List<WatchlistItem> items) {
        this.items = items;
    }

    public WatchlistItem get(int position) {
        return items.get(position);
    }

    public int size() {
        return items.size();
    }

}
