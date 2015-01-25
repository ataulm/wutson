package com.ataulm.wutson.model;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface TmdbApi {

    @GET("/configuration")
    Observable<Configuration> getConfiguration();

    @GET("/tv/popular")
    Observable<PopularShows> getPopularShows();

    @GET("/tv/{id}")
    Observable<TvShow> getTvShow(@Path("id") String id);

}
