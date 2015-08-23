package com.ataulm.wutson.episodes;

class EpisodeNumberDetail implements Detail {

    private final SeasonEpisodeNumber episodeNumber;

    EpisodeNumberDetail(SeasonEpisodeNumber episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public SeasonEpisodeNumber getEpisodeNumber() {
        return episodeNumber;
    }

    @Override
    public Type getType() {
        return Type.EPISODE_NUMBER;
    }

}
