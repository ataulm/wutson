package com.ataulm.wutson.repository;

import com.ataulm.wutson.model.Configuration;
import com.ataulm.wutson.model.DiscoverTvShows;
import com.ataulm.wutson.model.Genre;
import com.ataulm.wutson.model.Genres;
import com.ataulm.wutson.model.PopularShows;
import com.ataulm.wutson.model.TmdbApi;
import com.ataulm.wutson.model.TvShow;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class DataRepository {

    private final TmdbApi api;
    private final ConfigurationRepository configurationRepository;
    private final PopularShowsRepository popularShowsRepository;
    private final GenresRepository genresRepository;

    public DataRepository(TmdbApi api) {
        this.api = api;

        this.genresRepository = new GenresRepository(api);
        this.configurationRepository = new ConfigurationRepository(api);
        this.popularShowsRepository = new PopularShowsRepository(api);
    }

    private Observable<Configuration> getConfiguration() {
        return configurationRepository.getConfiguration();
    }

    private Observable<Genres> getGenres() {
        return genresRepository.getGenres();
    }

    public Observable<List<DiscoverTvShows>> getDiscoverTvShows() {
        return getGenres().map(new Func1<Genres, Observable<Genre>>() {

            @Override
            public Observable<Genre> call(Genres genres) {
                return Observable.from(genres);
            }

        }).flatMap(new Func1<Observable<Genre>, Observable<DiscoverTvShows>>() {

            @Override
            public Observable<DiscoverTvShows> call(Observable<Genre> genreObservable) {
                return genreObservable.flatMap(new Func1<Genre, Observable<DiscoverTvShows>>() {

                    @Override
                    public Observable<DiscoverTvShows> call(Genre genre) {
                        return api.getShowsMatchingGenre(genre.getId());
                    }

                });
            }

        }).toList();
    }

    public Observable<PopularShows> getPopularShows() {
        return popularShowsRepository.getPopularShows();
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
