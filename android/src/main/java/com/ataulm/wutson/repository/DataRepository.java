package com.ataulm.wutson.repository;

import com.ataulm.wutson.browseshows.ShowsInGenre;
import com.ataulm.wutson.browseshows.ShowsInGenreRepository;
import com.ataulm.wutson.model.Configuration;
import com.ataulm.wutson.model.TmdbApi;
import com.ataulm.wutson.model.TvShow;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class DataRepository {

    private final TmdbApi api;
    private final ConfigurationRepository configurationRepository;
    private final ShowsInGenreRepository showsInGenreRepository;

    public DataRepository(TmdbApi api) {
        this.api = api;

        this.configurationRepository = new ConfigurationRepository(api);
        this.showsInGenreRepository = new ShowsInGenreRepository(api);
    }

    private Observable<Configuration> getConfiguration() {
        return configurationRepository.getConfiguration();
    }

    public Observable<List<ShowsInGenre>> getShowsSeparatedByGenre() {
        return showsInGenreRepository.getShowsSeparatedByGenre();
    }

    public Observable<TvShow> getTvShow(final String showId) {
        return getConfiguration().flatMap(getTvShowWith(showId));
    }

    private Func1<Configuration, Observable<TvShow>> getTvShowWith(final String id) {
        return new Func1<Configuration, Observable<TvShow>>() {

            @Override
            public Observable<TvShow> call(final Configuration configuration) {
                return api.getTvShow(id).map(updateTvShowWith(configuration));
            }

        };
    }

    private Func1<TvShow, TvShow> updateTvShowWith(final Configuration configuration) {
        return new Func1<TvShow, TvShow>() {

            @Override
            public TvShow call(TvShow tvShow) {
                tvShow.setConfiguration(configuration);
                return tvShow;
            }

        };
    }

}
