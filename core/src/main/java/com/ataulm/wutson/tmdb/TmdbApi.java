package com.ataulm.wutson.tmdb;

import com.ataulm.wutson.tmdb.gson.GsonConfiguration;
import com.ataulm.wutson.tmdb.gson.GsonDiscoverTv;
import com.ataulm.wutson.tmdb.gson.GsonGenres;
import com.ataulm.wutson.tmdb.gson.GsonSearchTvResults;
import com.ataulm.wutson.tmdb.gson.GsonSeason;
import com.ataulm.wutson.tmdb.gson.GsonTvShow;

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
    Observable<GsonDiscoverTv> getShowsMatchingGenre(@Query("with_genres") String genreId);

    @GET("/tv/{id}?append_to_response=credits")
    Observable<GsonTvShow> getTvShow(@Path("id") String id);

    @GET("/tv/{id}/season/{season_number}")
    Observable<GsonSeason> getSeason(@Path("id") String showId, @Path("season_number") int seasonNumber);

    @GET("/search/tv/")
    Observable<GsonSearchTvResults> getSearchTvResults(@Query("query") String searchQuery);

}
