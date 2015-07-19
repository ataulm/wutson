package com.ataulm.wutson.jabber;

import android.app.Application;
import android.content.Context;

import com.ataulm.wutson.AndroidLog;
import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.Log;
import com.ataulm.wutson.ToastDisplayer;
import com.ataulm.wutson.repository.DataRepository;
import com.ataulm.wutson.shows.discover.DiscoverShowsRepository;
import com.ataulm.wutson.shows.myshows.SearchRepository;
import com.ataulm.wutson.trakt.TraktApi;
import com.ataulm.wutson.trakt.TraktApiFactory;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;

import retrofit.client.Client;
import retrofit.client.OkClient;

public final class Jabber {

    private static final int MAX_CACHE_SIZE_BYTES = 10 * 1024 * 1024;

    private static Jabber instance;

    private final Context context;
    private final String tmdbApiKey;
    private final String traktApiKey;

    private Repositories repositories;
    private ToastDisplayer toastDisplayer;
    private Log log;

    public static void init(Application application, String tmdbApiKey, String traktApiKey) {
        instance = new Jabber(application.getApplicationContext(), tmdbApiKey, traktApiKey);
    }

    private Jabber(Context context, String tmdbApiKey, String traktApiKey) {
        this.context = context;
        this.tmdbApiKey = tmdbApiKey;
        this.traktApiKey = traktApiKey;
    }

    private static Repositories repositories() {
        if (instance.repositories == null) {
            Client client = newClient();
            TraktApi traktApi = newTraktApi(instance.traktApiKey, log(), client);
            instance.repositories = Repositories.newInstance(instance.context, traktApi);
        }
        return instance.repositories;
    }

    public static DataRepository dataRepository() {
        return repositories().dataRepository();
    }

    public static DiscoverShowsRepository discoverShowsRepository() {
        return repositories().discoverShows();
    }

    public static SearchRepository searchRepository() {
        return repositories().search();
    }

    private static TraktApi newTraktApi(String traktApiKey, Log log, Client client) {
        boolean enableLogs = BuildConfig.DEBUG;
        TraktApiFactory traktApiFactory = TraktApiFactory.newInstance(traktApiKey, client, enableLogs, log);
        return traktApiFactory.createApi();
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

    public static Log log() {
        if (instance.log == null) {
            instance.log = new AndroidLog();
        }
        return instance.log;
    }

}
