package com.ataulm.wutson;

import android.app.Application;

import com.ataulm.wutson.model.TmdbApi;
import com.ataulm.wutson.model.TmdbApiFactory;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;

import retrofit.client.Client;
import retrofit.client.OkClient;

public class WutsonApplication extends Application {

    private static final int MAX_CACHE_SIZE = 1024;

    private DataRepository dataRepository;

    public DataRepository getDataRepository() {
        if (dataRepository == null) {
            TmdbApiFactory tmdbApiFactory = TmdbApiFactory.newInstance(BuildConfig.TMDB_API_KEY, newClient());
            TmdbApi api = tmdbApiFactory.createApi();
            dataRepository = new DataRepository(api);
        }
        return dataRepository;
    }

    private Client newClient() {
        OkHttpClient client = new OkHttpClient();
        try {
            Cache cache = new Cache(getCacheDir(), MAX_CACHE_SIZE);
            return new OkClient(client.setCache(cache));
        } catch (IOException e) {
            return new OkClient(client);
        }
    }

}
