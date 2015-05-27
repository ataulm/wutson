package com.ataulm.wutson.model;

public class EpisodesByDay {

    private final String day;
    private final Episodes episodes;

    public EpisodesByDay(String day, Episodes episodes) {
        this.day = day;
        this.episodes = episodes;
    }

    public String getDay() {
        return day;
    }

    public Episodes getEpisodes() {
        return episodes;
    }

}
