package com.ataulm.wutson.myshows;

import java.util.List;

public class Watchlist {

    private final List<WatchlistItem> items;

    public Watchlist(List<WatchlistItem> items) {
        this.items = items;
    }

    WatchlistItem get(int position) {
        return items.get(position);
    }

    int size() {
        return items.size();
    }

}
