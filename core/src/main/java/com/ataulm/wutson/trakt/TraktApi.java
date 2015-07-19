package com.ataulm.wutson.trakt;

import com.ataulm.wutson.trakt.gson.GsonShowSummary;
import com.ataulm.wutson.trakt.gson.GsonTrendingShow;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface TraktApi {

    @GET("/shows/trending?extended=images")
    Observable<List<GsonTrendingShow>> getTrendingShows();

    @GET("/shows/popular?extended=images")
    Observable<List<GsonShowSummary>> getPopularShows();

    @GET("/shows/{id}?extended=images,full")
    Observable<GsonShowDetails> getShowDetails(@Path("id") String showId);

    @GET("/shows/{id}/seasons?extended=images,episodes,full")
    Observable<GsonShowSeasonsList> getShowSeasons(@Path("id") String showId);

}
