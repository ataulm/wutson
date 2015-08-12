package com.ataulm.wutson.trakt;

import com.ataulm.wutson.Log;

import retrofit.Endpoint;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;

public class TraktApiFactory {

    private static final String ENDPOINT_URL = "https://api-v2launch.trakt.tv";
    private static final String ENDPOINT_NAME = "trakt_v2";
    private static final String API_VERSION = "2";

    private final RestAdapter restAdapter;

    private TraktApiFactory(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    public static TraktApiFactory newInstance(final String apiKey, Client client, boolean enableLogs, final Log log) {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {

            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("trakt-api-key", apiKey);
                request.addHeader("trakt-api-version", API_VERSION);
            }

        };

        Endpoint endpoint = new Endpoint() {

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
                .setEndpoint(endpoint)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String message) {
                        log.verbose("retrofit", message);
                    }
                })
                .setClient(client)
                .setRequestInterceptor(requestInterceptor)
                .build();

        if (enableLogs) {
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        }

        return new TraktApiFactory(restAdapter);
    }

    public TraktApi createApi() {
        return restAdapter.create(TraktApi.class);
    }

}
