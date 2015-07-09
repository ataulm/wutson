package com.ataulm.wutson.shows.myshows;

import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonSearchTvResults;

import rx.Observable;

public class SearchRepository {

    private final TmdbApi api;

    public SearchRepository(TmdbApi api) {
        this.api = api;
    }

    public Observable<GsonSearchTvResults> searchFor(String query) {
        return api.getSearchTvResults(query);
    }

}
