package com.ataulm.wutson.jabber;

import android.content.Context;

import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.repository.DataRepository;
import com.ataulm.wutson.repository.SeasonsRepository;
import com.ataulm.wutson.repository.ShowRepository;
import com.ataulm.wutson.repository.TrackedShowsRepository;
import com.ataulm.wutson.repository.WutsonDataRepository;
import com.ataulm.wutson.repository.persistence.JsonRepository;
import com.ataulm.wutson.repository.persistence.LocalDataRepository;
import com.ataulm.wutson.repository.persistence.SqliteJsonRepository;
import com.ataulm.wutson.repository.persistence.SqliteLocalDataRepository;
import com.ataulm.wutson.shows.discover.DiscoverShowsRepository;
import com.ataulm.wutson.shows.myshows.SearchRepository;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.trakt.TraktApi;
import com.google.gson.Gson;

final class Repositories {

    private final Context context;
    private final TmdbApi tmdbApi;
    private final TraktApi traktApi;

    private WutsonDataRepository dataRepository;
    private SearchRepository searchRepository;
    private DiscoverShowsRepository discoverShows;
    private ConfigurationRepository configuration;
    private LocalDataRepository localData;
    private JsonRepository jsonRepository;
    private Gson gson;

    static Repositories newInstance(Context context, TmdbApi tmdbApi, TraktApi traktApi) {
        return new Repositories(context.getApplicationContext(), tmdbApi, traktApi);
    }

    private Repositories(Context context, TmdbApi tmdbApi, TraktApi traktApi) {
        this.context = context;
        this.tmdbApi = tmdbApi;
        this.traktApi = traktApi;
    }

    public DiscoverShowsRepository discoverShows() {
        if (discoverShows == null) {
            discoverShows = new DiscoverShowsRepository(traktApi);
        }
        return discoverShows;
    }

    private ConfigurationRepository configuration() {
        if (configuration == null) {
            configuration = new ConfigurationRepository(tmdbApi, localData(), gson());
        }
        return configuration;
    }

    public SearchRepository search() {
        if (searchRepository == null) {
            searchRepository = new SearchRepository(tmdbApi, configuration());
        }
        return searchRepository;
    }

    private LocalDataRepository localData() {
        if (localData == null) {
            localData = new SqliteLocalDataRepository(context.getContentResolver());
        }
        return localData;
    }

    private JsonRepository jsonRepository() {
        if (jsonRepository == null) {
            jsonRepository = new SqliteJsonRepository(context.getContentResolver());
        }
        return jsonRepository;
    }

    private Gson gson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public DataRepository dataRepository() {
        if (dataRepository == null) {
            Gson gson = gson();
            LocalDataRepository persistentDataRepo = localData();
            JsonRepository jsonRepository = jsonRepository();
            ConfigurationRepository configurationRepo = configuration();

            TrackedShowsRepository trackedShowsRepo = new TrackedShowsRepository(persistentDataRepo, configurationRepo, gson);
            ShowRepository showRepo = new ShowRepository(traktApi, jsonRepository, tmdbApi, persistentDataRepo, configurationRepo, gson);
            SeasonsRepository seasonsRepo = new SeasonsRepository(tmdbApi, persistentDataRepo, configurationRepo, showRepo, gson);

            dataRepository = new WutsonDataRepository(trackedShowsRepo, showRepo, seasonsRepo);
        }
        return dataRepository;
    }

}
