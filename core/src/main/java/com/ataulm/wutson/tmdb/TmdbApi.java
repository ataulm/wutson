package com.ataulm.wutson.tmdb;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface TmdbApi {

    @GET("/configuration")
    Observable<Configuration> getConfiguration();

    @GET("/genre/tv/list")
    Observable<Genres> getGenres();

    @GET("/discover/tv")
    Observable<DiscoverTvShows> getShowsMatchingGenre(@Query("with_genres") String genreId);

    @GET("/tv/{id}?append_to_response=credits")
    Observable<TvShow> getTvShow(@Path("id") String id);

}
