package com.ataulm.wutson.popularshows;

import retrofit.Endpoint;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public final class TmdbRestAdapter {

    private static final String QUERY_PARAM_API_KEY = "api_key";
    private static final String ENDPOINT_URL = "https://api.themoviedb.org/3";
    private static final String ENDPOINT_NAME = "tmdb";

    private final RestAdapter restAdapter;

    public TmdbRestAdapter newInstance(final String apiKey) {
        RequestInterceptor tmdbRequestInterceptor = new RequestInterceptor() {

            @Override
            public void intercept(RequestFacade request) {
                request.addQueryParam(QUERY_PARAM_API_KEY, apiKey);
            }

        };

        Endpoint tmdbEndpoint = new Endpoint() {

            @Override
            public String getUrl() {
                return ENDPOINT_URL;
            }

            @Override
            public String getName() {
                return ENDPOINT_NAME;
            }

        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(tmdbEndpoint)
                .setRequestInterceptor(tmdbRequestInterceptor)
                .build();

        return new TmdbRestAdapter(restAdapter);
    }

    private TmdbRestAdapter(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

}
