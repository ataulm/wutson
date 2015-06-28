package com.ataulm.wutson.repository.persistence;

import com.ataulm.wutson.shows.ShowId;

import java.util.List;

public interface LocalDataRepository {
    String readJsonConfiguration();

    void writeJsonConfiguration(String json);

    String readJsonGenres();

    void writeJsonGenres(String json);

    String readJsonShowSummaries(String tmdbGenreId);

    void writeJsonShowSummary(String tmdbGenreId, String json);

    boolean isShowTracked(ShowId tmdbShowId);

    void addToTrackedShows(ShowId tmdbShowId);

    int deleteFromTrackedShows(ShowId tmdbShowId);

    String readJsonSeason(ShowId tmdbShowId, int seasonNumber);

    void writeJsonSeason(ShowId tmdbShowId, int seasonNumber, String json);

    String readJsonShowDetails(ShowId tmdbShowId);

    void writeJsonShowDetails(ShowId tmdbShowId, String json);

    List<ShowId> getListOfTmdbShowIdsFromAllTrackedShows();
}
