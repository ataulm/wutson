package com.ataulm.wutson;

import android.app.Application;
import android.content.Context;

import com.ataulm.wutson.model.TmdbApi;
import com.ataulm.wutson.model.TmdbApiFactory;
import com.ataulm.wutson.repository.DataRepository;
import com.ataulm.wutson.shots.Swatches;
import com.ataulm.wutson.shots.ToastDisplayer;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;

import retrofit.client.Client;
import retrofit.client.OkClient;

public final class Jabber {

    private static final int MAX_CACHE_SIZE = 1024;

    private static Jabber instance;

    private final Context context;

    private DataRepository dataRepository;
    private ToastDisplayer toastDisplayer;
    private Swatches swatches;

    public static void init(Application application) {
        instance = new Jabber(application.getApplicationContext());
    }

    private Jabber(Context context) {
        this.context = context;
    }

    public static Swatches swatches() {
        if (instance.swatches == null) {
            instance.swatches = new Swatches();
        }
        return instance.swatches;
    }

    public static DataRepository dataRepository() {
        if (instance.dataRepository == null) {
            boolean enableLogs = BuildConfig.DEBUG;
            TmdbApiFactory tmdbApiFactory = TmdbApiFactory.newInstance(BuildConfig.TMDB_API_KEY, newClient(), enableLogs);
            TmdbApi api = tmdbApiFactory.createApi();
            instance.dataRepository = new DataRepository(api);
        }
        return instance.dataRepository;
    }

    private static Client newClient() {
        OkHttpClient client = new OkHttpClient();
        try {
            Cache cache = new Cache(instance.context.getCacheDir(), MAX_CACHE_SIZE);
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
