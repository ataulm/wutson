package com.ataulm.wutson;

import android.app.Application;

import com.ataulm.wutson.model.TmdbApi;
import com.ataulm.wutson.model.TmdbApiFactory;

public class WutsonApplication extends Application {

    private DataRepository dataRepository;

    public DataRepository getDataRepository() {
        if (dataRepository == null) {
            TmdbApiFactory tmdbApiFactory = TmdbApiFactory.newInstance(BuildConfig.TMDB_API_KEY);
            TmdbApi api = tmdbApiFactory.createApi();
            dataRepository = new DataRepository(api);
        }
        return dataRepository;
    }

}
