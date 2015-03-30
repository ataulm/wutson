package com.ataulm.wutson;

import android.app.Application;
import android.content.Context;

import com.ataulm.wutson.discover.GenresRepository;
import com.ataulm.wutson.discover.ShowsInGenreRepository;
import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.repository.DataRepository;
import com.ataulm.wutson.repository.TrackedShowsRepository;
import com.ataulm.wutson.repository.persistence.PersistentDataRepository;
import com.ataulm.wutson.seasons.SeasonsRepository;
import com.ataulm.wutson.showdetails.ShowRepository;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.TmdbApiFactory;
import com.google.gson.Gson;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;

import retrofit.client.Client;
import retrofit.client.OkClient;

public final class Jabber {

    private static final int MAX_CACHE_SIZE_BYTES = 10 * 1024 * 1024;

    private static Jabber instance;

    private final Context context;

    private DataRepository dataRepository;
    private ToastDisplayer toastDisplayer;

    public static void init(Application application) {
        instance = new Jabber(application.getApplicationContext());
    }

    private Jabber(Context context) {
        this.context = context;
    }

    public static DataRepository dataRepository() {
        if (instance.dataRepository == null) {
            TmdbApi api = newApi();
            Gson gson = new Gson();
            PersistentDataRepository persistentDataRepo = new PersistentDataRepository(instance.context.getContentResolver());
            ConfigurationRepository configurationRepo = new ConfigurationRepository(api, persistentDataRepo, gson);
            TrackedShowsRepository trackedShowsRepo = new TrackedShowsRepository(persistentDataRepo, gson);
            GenresRepository genresRepo = new GenresRepository(api, persistentDataRepo, gson);
            ShowsInGenreRepository showsInGenreRepo = new ShowsInGenreRepository(api, persistentDataRepo, configurationRepo, genresRepo, gson);
            ShowRepository showRepo = new ShowRepository(api, persistentDataRepo, configurationRepo, gson);
            SeasonsRepository seasonsRepo = new SeasonsRepository(api, configurationRepo, showRepo);

            instance.dataRepository = new DataRepository(trackedShowsRepo, showsInGenreRepo, showRepo, seasonsRepo);
        }
        return instance.dataRepository;
    }

    private static TmdbApi newApi() {
        boolean enableLogs = BuildConfig.DEBUG;
        TmdbApiFactory tmdbApiFactory = TmdbApiFactory.newInstance(BuildConfig.TMDB_API_KEY, newClient(), enableLogs);
        return tmdbApiFactory.createApi();
    }

    private static Client newClient() {
        OkHttpClient client = new OkHttpClient();
        try {
            Cache cache = new Cache(instance.context.getCacheDir(), MAX_CACHE_SIZE_BYTES);
            return new OkClient(client.setCache(cache));
        } catch (IOException e) {
            return new OkClient(client);
        }
    }

    public static ToastDisplayer toastDisplayer() {
        if (instance.toastDisplayer == null) {
            instance.toastDisplayer = new ToastDisplayer(instance.context);
        }
        return instance.toastDisplayer;
    }

}
