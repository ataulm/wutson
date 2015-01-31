package com.ataulm.wutson.model;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface TmdbApi {

    @GET("/configuration")
    Observable<Configuration> getConfiguration();

    @GET("/genre/tv/list")
    Observable<Genres> getGenres();

    @GET("/tv/popular")
    Observable<PopularShows> getPopularShows();

    @GET("/discover/tv")
    Observable<DiscoverTvShows> getShowsMatchingGenre(@Query("with_genres") String genreId);

    @GET("/tv/{id}")
    Observable<TvShow> getTvShow(@Path("id") String id);

}
