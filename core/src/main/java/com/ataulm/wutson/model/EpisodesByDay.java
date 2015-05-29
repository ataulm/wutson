package com.ataulm.wutson.model;

public class EpisodesByDay {

    private final SimpleDate date;
    private final Episodes episodes;

    public EpisodesByDay(SimpleDate date, Episodes episodes) {
        this.date = date;
        this.episodes = episodes;
    }

    public SimpleDate getDate() {
        return date;
    }

    public Episodes getEpisodes() {
        return episodes;
    }

}
