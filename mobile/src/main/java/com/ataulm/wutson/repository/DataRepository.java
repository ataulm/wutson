package com.ataulm.wutson.repository;

import com.ataulm.wutson.discover.ShowsInGenre;
import com.ataulm.wutson.discover.ShowsInGenreRepository;
import com.ataulm.wutson.model.Configuration;
import com.ataulm.wutson.model.TmdbApi;
import com.ataulm.wutson.model.TvShow;
import com.ataulm.wutson.show.Cast;
import com.ataulm.wutson.show.Show;

import java.net.URI;
import java.util.Collections;
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

    public Observable<Show> getShow(final String showId) {
        return getConfiguration().flatMap(getTvShowWith(showId)).map(new Func1<TvShow, Show>() {
            @Override
            public Show call(TvShow tvShow) {
                String name = tvShow.getName();
                String overview = tvShow.getOverview();
                URI posterUri = URI.create(tvShow.getPosterPath());
                Cast cast = new Cast(Collections.EMPTY_LIST);
                return new Show(name, overview, posterUri, cast);
            }
        });
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
