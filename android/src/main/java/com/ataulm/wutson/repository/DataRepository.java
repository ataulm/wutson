package com.ataulm.wutson.repository;

import com.ataulm.wutson.model.Configuration;
import com.ataulm.wutson.model.PopularShows;
import com.ataulm.wutson.model.TmdbApi;
import com.ataulm.wutson.model.TvShow;

import rx.Observable;

public class DataRepository {

    private final TmdbApi api;
    private final ConfigurationRepository configurationRepository;
    private final PopularShowsRepository popularShowsRepository;

    public DataRepository(TmdbApi api) {
        this.api = api;
        this.configurationRepository = new ConfigurationRepository(api);
        this.popularShowsRepository = new PopularShowsRepository(api);
    }

    public Observable<Configuration> getConfiguration() {
        return configurationRepository.getConfiguration();
    }

    public Observable<PopularShows> getPopularShows() {
        return popularShowsRepository.getPopularShows();
    }

    public Observable<TvShow> getTvShow(String id) {
        return api.getTvShow(id);
    }

}
