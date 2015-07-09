package com.ataulm.wutson.shows.myshows;

import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonSearchTvResults;

import java.util.Collections;

import rx.Observable;
import rx.functions.Func1;

public class SearchRepository {

    private final TmdbApi api;

    public SearchRepository(TmdbApi api) {
        this.api = api;
    }

    public Observable<SearchTvResults> searchFor(String query) {
        return api.getSearchTvResults(query).map(asSearchTvResults());
    }

    private static Func1<GsonSearchTvResults, SearchTvResults> asSearchTvResults() {
        return new Func1<GsonSearchTvResults, SearchTvResults>() {
            @Override
            public SearchTvResults call(GsonSearchTvResults gsonSearchTvResults) {
                return new SearchTvResults(Collections.<SearchTvResult>emptyList());
            }
        };
    }

}
