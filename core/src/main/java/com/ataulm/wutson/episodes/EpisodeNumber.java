package com.ataulm.wutson.episodes;

public class EpisodeNumber {

    private static final String FORMAT_LEADING_ZEROES = "%02d";

    private final int seasonNumber;
    private final int episodeNumber;

    public EpisodeNumber(int seasonNumber, int episodeNumber) {
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
    }

    @Override
    public String toString() {
        return "S" + String.format(FORMAT_LEADING_ZEROES, seasonNumber) + "E" + String.format(FORMAT_LEADING_ZEROES, episodeNumber);
    }

    public int getSeason() {
        return seasonNumber;
    }

    public int getEpisode() {
        return episodeNumber;
    }

}
