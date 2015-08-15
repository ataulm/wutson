package com.ataulm.wutson.episodes;

import android.net.Uri;

import com.ataulm.wutson.shows.ShowId;

public class EpisodesUri {

    private static final int URI_PATH_SEGMENT_SHOW_ID_INDEX = 1;
    private static final int URI_PATH_SEGMENT_SEASON_NUMBER_INDEX = 3;
    private static final int URI_PATH_SEGMENT_EPISODE_NUMBER_INDEX = 5;

    private final ShowId showId;
    private final SeasonEpisodeNumber seasonEpisodeNumber;

    static EpisodesUri from(Uri uri) {
        ShowId showId = new ShowId(uri.getPathSegments().get(URI_PATH_SEGMENT_SHOW_ID_INDEX));
        int seasonNumber = Integer.parseInt(uri.getPathSegments().get(URI_PATH_SEGMENT_SEASON_NUMBER_INDEX));
        int episodeNumber = Integer.parseInt(uri.getPathSegments().get(URI_PATH_SEGMENT_EPISODE_NUMBER_INDEX));
        SeasonEpisodeNumber seasonEpisodeNumber = new SeasonEpisodeNumber(seasonNumber, episodeNumber);

        return new EpisodesUri(showId, seasonEpisodeNumber);
    }

    public EpisodesUri(ShowId showId, SeasonEpisodeNumber seasonEpisodeNumber) {
        this.showId = showId;
        this.seasonEpisodeNumber = seasonEpisodeNumber;
    }

    public ShowId getShowId() {
        return showId;
    }

    public int getSeasonNumber() {
        return seasonEpisodeNumber.getSeason();
    }

    public int getSeasonEpisodeNumber() {
        return seasonEpisodeNumber.getEpisode();
    }

    public Uri buildUriUpon(Uri baseUri) {
        return baseUri.buildUpon()
                .appendPath("show").appendPath(showId.toString())
                .appendPath("season").appendPath(String.valueOf(seasonEpisodeNumber.getSeason()))
                .appendPath("episode").appendPath(String.valueOf(seasonEpisodeNumber.getEpisode()))
                .build();
    }

}
