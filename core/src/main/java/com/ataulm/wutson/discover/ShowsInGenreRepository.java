package com.ataulm.wutson.discover;

import com.ataulm.wutson.model.Configuration;
import com.ataulm.wutson.model.DiscoverTvShows;
import com.ataulm.wutson.model.Genre;
import com.ataulm.wutson.model.TmdbApi;
import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.rx.Functions;
import com.ataulm.wutson.rx.InfiniteOperator;

import java.net.URI;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class ShowsInGenreRepository {

    private final TmdbApi api;
    private final GenresRepository genresRepository;
    private final ConfigurationRepository configurationRepository;
    private final BehaviorSubject<List<ShowsInGenre>> subject;

    public ShowsInGenreRepository(TmdbApi api, ConfigurationRepository configurationRepository) {
        this.api = api;
        this.configurationRepository = configurationRepository;

        this.genresRepository = new GenresRepository(api);
        this.subject = BehaviorSubject.create();
    }

    public Observable<List<ShowsInGenre>> getShowsSeparatedByGenre() {
        if (!subject.hasValue()) {
            refreshBrowseShows();
        }
        return subject;
    }

    private void refreshBrowseShows() {
        Observable<Genre> genreObservable = genresRepository.getGenres().flatMap(Functions.<Genre>iterate());
        Observable<DiscoverTvShows> discoverTvShowsObservable = genreObservable.flatMap(fetchDiscoverTvShows());
        Observable<List<Show>> showsListObservable = Observable.combineLatest(infiniteConfigurationObservable(), discoverTvShowsObservable, combineAsShowsList());
        Observable<ShowsInGenre> showsInGenreObservable = Observable.zip(genreObservable, showsListObservable, combineAsShowsInGenre());

        showsInGenreObservable.toList()
                .lift(new InfiniteOperator<List<ShowsInGenre>>())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    private Func2<Configuration, DiscoverTvShows, List<Show>> combineAsShowsList() {
        return new Func2<Configuration, DiscoverTvShows, List<Show>>() {

            @Override
            public List<Show> call(final Configuration configuration, DiscoverTvShows discoverTvShows) {
                return Observable.from(discoverTvShows).map(convertToShow(configuration)).toList().toBlocking().single();
            }

        };
    }

    private Func1<DiscoverTvShows.Show, Show> convertToShow(final Configuration configuration) {
        return new Func1<DiscoverTvShows.Show, Show>() {

            @Override
            public Show call(DiscoverTvShows.Show discoverTvShow) {
                String id = discoverTvShow.id;
                String name = discoverTvShow.name;
                URI posterUri = URI.create(configuration.getCompletePosterPath(discoverTvShow.posterPath));
                return new Show(id, name, posterUri);
            }

        };
    }

    private Func2<Genre, List<Show>, ShowsInGenre> combineAsShowsInGenre() {
        return new Func2<Genre, List<Show>, ShowsInGenre>() {
            @Override
            public ShowsInGenre call(Genre genre, List<Show> shows) {
                return new ShowsInGenre(genre, shows);
            }
        };
    }

    private Func1<Genre, Observable<DiscoverTvShows>> fetchDiscoverTvShows() {
        return new Func1<Genre, Observable<DiscoverTvShows>>() {

            @Override
            public Observable<DiscoverTvShows> call(Genre genre) {
                return api.getShowsMatchingGenre(genre.getId());
            }

        };
    }

    private Observable<Configuration> infiniteConfigurationObservable() {
        return configurationRepository.getConfiguration().first().lift(new InfiniteOperator<Configuration>());
    }

}
