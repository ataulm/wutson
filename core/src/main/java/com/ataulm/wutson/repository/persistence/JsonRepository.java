package com.ataulm.wutson.repository.persistence;

import com.ataulm.wutson.shows.ShowId;

public interface JsonRepository {

    String readTrendingShowsList();

    void writeTrendingShowsList(String json);

    String readPopularShowsList();

    void writePopularShowsList(String json);

    String readShowDetails(ShowId showId);

    void writeShowDetails(ShowId showId, String json);

    String readSeasons(ShowId showId);

    void writeSeasons(ShowId showId, String json);

}
