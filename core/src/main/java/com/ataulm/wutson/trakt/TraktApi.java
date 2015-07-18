package com.ataulm.wutson.trakt;

import com.ataulm.wutson.trakt.gson.GsonShow;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;

public interface TraktApi {

    @GET("/shows/trending?extended=images")
    Observable<List<GsonShow>> getTrendingShows();

}
