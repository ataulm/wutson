package com.ataulm.wutson.trakt;

import com.ataulm.wutson.trakt.gson.GsonShow;
import com.ataulm.wutson.trakt.gson.GsonTrendingShow;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;

public interface TraktApi {

    @GET("/shows/trending?extended=images")
    Observable<List<GsonTrendingShow>> getTrendingShows();

    @GET("/shows/popular?extended=images")
    Observable<List<GsonShow>> getPopularShows();

}
