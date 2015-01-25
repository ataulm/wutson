package com.ataulm.wutson.model;

import retrofit.Endpoint;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;

public class TmdbApiFactory {

    private static final String QUERY_PARAM_API_KEY = "api_key";
    private static final String ENDPOINT_URL = "https://api.themoviedb.org/3";
    private static final String ENDPOINT_NAME = "tmdb";

    private final RestAdapter restAdapter;

    public static TmdbApiFactory newInstance(final String apiKey, Client client) {
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

        return new TmdbApiFactory(restAdapter);
    }

    TmdbApiFactory(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    public TmdbApi createApi() {
        return restAdapter.create(TmdbApi.class);
    }

}
