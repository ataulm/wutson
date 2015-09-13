package com.ataulm.wutson.repository.persistence;

import com.ataulm.wutson.shows.ShowId;

public interface JsonRepository {

    long readTrendingShowsCreatedDate();

    String readTrendingShowsList();

    void writeTrendingShowsList(String json);

    long readPopularShowsCreatedDate();

    void writePopularShowsList(String json);

    String readPopularShowsList();

    String readShowDetails(ShowId showId);

    void writeShowDetails(ShowId showId, String json);

    String readSeasons(ShowId showId);

    void writeSeasons(ShowId showId, String json);

}
