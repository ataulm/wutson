package com.ataulm.wutson.trakt;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface TraktApi {

    @GET("/shows/trending?extended=images&limit=100")
    Observable<GsonTrendingShowList> getTrendingShows();

    @GET("/shows/popular?extended=images&limit=100")
    Observable<GsonPopularShowList> getPopularShows();

    @GET("/shows/{id}?extended=images,full")
    Observable<GsonShowDetails> getShowDetails(@Path("id") String showId);

    @GET("/shows/{id}/people?extended=images,full")
    Observable<GsonShowPeople> getShowPeople(@Path("id") String showId);

    @GET("/shows/{id}/seasons?extended=images,episodes,full")
    Observable<GsonShowSeasonList> getShowSeasons(@Path("id") String showId);

    @GET("/search?type=show&query={query}")
    Observable<GsonSearchResults> getSearchResults(@Path("query") String query);

}
