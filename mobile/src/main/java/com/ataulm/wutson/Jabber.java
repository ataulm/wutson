package com.ataulm.wutson;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.ataulm.wutson.repository.DataRepository;
import com.ataulm.wutson.repository.persistence.PersistentDataRepository;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.TmdbApiFactory;
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
            boolean enableLogs = BuildConfig.DEBUG;
            TmdbApiFactory tmdbApiFactory = TmdbApiFactory.newInstance(BuildConfig.TMDB_API_KEY, newClient(), enableLogs);
            TmdbApi api = tmdbApiFactory.createApi();
            SharedPreferences sharedPreferences = instance.context.getSharedPreferences("key_prefs_tracked_shows", Context.MODE_PRIVATE);
            PersistentDataRepository persistentDataRepository = new PersistentDataRepository(instance.context.getContentResolver());

            instance.dataRepository = new DataRepository(api, sharedPreferences, persistentDataRepository);
        }
        return instance.dataRepository;
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
