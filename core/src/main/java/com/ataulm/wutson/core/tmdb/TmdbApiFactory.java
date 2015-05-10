package com.ataulm.wutson.core.tmdb;

import retrofit.Endpoint;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;

public class TmdbApiFactory {

    private static final String QUERY_PARAM_API_KEY = "api_key";
    private static final String ENDPOINT_URL = "https://api.themoviedb.org/3";
    private static final String ENDPOINT_NAME = "tmdb";

    private final RestAdapter restAdapter;

    private TmdbApiFactory(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    public static TmdbApiFactory newInstance(final String apiKey, Client client, boolean enableLogs) {
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
                .setClient(client)
                .setRequestInterceptor(tmdbRequestInterceptor)
                .build();

        if (enableLogs) {
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        }

        return new TmdbApiFactory(restAdapter);
    }

    public TmdbApi createApi() {
        return restAdapter.create(TmdbApi.class);
    }

}
