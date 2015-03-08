package com.ataulm.wutson.tmdb;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface TmdbApi {

    @GET("/configuration")
    Observable<GsonConfiguration> getConfiguration();

    @GET("/genre/tv/list")
    Observable<GsonGenres> getGenres();

    @GET("/discover/tv")
    Observable<GsonDiscoverTvShows> getShowsMatchingGenre(@Query("with_genres") String genreId);

    @GET("/tv/{id}?append_to_response=credits")
    Observable<TvShow> getTvShow(@Path("id") String id);

}
