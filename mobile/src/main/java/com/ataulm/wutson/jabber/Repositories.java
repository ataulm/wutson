package com.ataulm.wutson.jabber;

import android.content.Context;

import com.ataulm.wutson.Log;
import com.ataulm.wutson.repository.DataRepository;
import com.ataulm.wutson.repository.ShowRepository;
import com.ataulm.wutson.repository.TrackedShowsRepository;
import com.ataulm.wutson.repository.WutsonDataRepository;
import com.ataulm.wutson.repository.persistence.JsonRepository;
import com.ataulm.wutson.repository.persistence.SqliteJsonRepository;
import com.ataulm.wutson.shows.discover.DiscoverShowsRepository;
import com.ataulm.wutson.trakt.TraktApi;
import com.google.gson.Gson;

final class Repositories {

    private final Context context;
    private final TraktApi traktApi;
    private final Log log;

    private DiscoverShowsRepository discoverShowsRepository;
    private ShowRepository showRepository;
    private JsonRepository jsonRepository;
    private Gson gson;

    @Deprecated // TODO no more super repo! Aim to get rid of this in favour of a more specific repos - MyShowsRepository, TraktRepo?
    private WutsonDataRepository dataRepository;

    static Repositories newInstance(Context context, TraktApi traktApi, Log log) {
        return new Repositories(context.getApplicationContext(), traktApi, log);
    }

    private Repositories(Context context, TraktApi traktApi, Log log) {
        this.context = context;
        this.traktApi = traktApi;
        this.log = log;
    }

    public DiscoverShowsRepository discoverShowsRepository() {
        if (discoverShowsRepository == null) {
            JsonRepository jsonRepository = jsonRepository();
            Gson gson = gson();
            discoverShowsRepository = new DiscoverShowsRepository(traktApi, jsonRepository, gson, log);
        }
        return discoverShowsRepository;
    }

    public ShowRepository showRepository() {
        if (showRepository == null) {
            JsonRepository jsonRepository = jsonRepository();
            Gson gson = gson();
            showRepository = new ShowRepository(traktApi, jsonRepository, gson);
        }
        return showRepository;
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
            JsonRepository jsonRepository = jsonRepository();

            TrackedShowsRepository trackedShowsRepo = new TrackedShowsRepository(gson);
            ShowRepository showRepo = new ShowRepository(traktApi, jsonRepository, gson);

            dataRepository = new WutsonDataRepository(trackedShowsRepo, showRepo);
        }
        return dataRepository;
    }

}
