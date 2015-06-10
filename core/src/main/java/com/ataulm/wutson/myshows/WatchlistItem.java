package com.ataulm.wutson.myshows;

import com.ataulm.wutson.model.Episode;

public final class WatchlistItem<T> {

    private final Type type;
    private final T item;

    public static WatchlistItem<String> from(String showName) {
        return new WatchlistItem<>(Type.SHOW, showName);
    }

    public static WatchlistItem<Episode> from(Episode episode) {
        return new WatchlistItem<>(Type.EPISODE, episode);
    }

    private WatchlistItem(Type type, T item) {
        this.type = type;
        this.item = item;
    }

    boolean isShow() {
        return type == Type.SHOW;
    }

    boolean isEpisode() {
        return type == Type.EPISODE;
    }

    T getItem() {
        return item;
    }

    private enum Type {
        SHOW,
        EPISODE
    }

}
