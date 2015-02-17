package com.ataulm.wutson.discover;

import com.ataulm.wutson.model.Configuration;
import com.ataulm.wutson.model.DiscoverTvShows;
import com.ataulm.wutson.model.Genre;
import com.ataulm.wutson.model.TmdbApi;
import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.rx.Functions;
import com.ataulm.wutson.rx.InfiniteOperator;

import java.net.URI;
import java.util.ArrayList;
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
        Observable<DiscoverTvShowsInGenre> discoverTvShowsObservable = genreObservable.flatMap(fetchDiscoverTvShows());

        Observable<ShowsInGenre> showsInGenreObservable = Observable.combineLatest(configurationObservable(), discoverTvShowsObservable, new Func2<Configuration, DiscoverTvShowsInGenre, ShowsInGenre>() {

            @Override
            public ShowsInGenre call(Configuration configuration, DiscoverTvShowsInGenre discoverTvShows) {
                Genre genre = discoverTvShows.genre;
                List<Show> shows = new ArrayList<>(discoverTvShows.shows.size());
                for (DiscoverTvShows.Show discoverTvShow : discoverTvShows.shows) {
                    String id = discoverTvShow.id;
                    String name = discoverTvShow.name;
                    URI posterUri = URI.create(configuration.getCompletePosterPath(discoverTvShow.posterPath));

                    shows.add(new Show(id, name, posterUri));
                }
                ShowsInGenre showsInGenre = new ShowsInGenre(genre, shows);
                return showsInGenre;
            }

        });

        showsInGenreObservable.toList()
                .lift(new InfiniteOperator<List<ShowsInGenre>>())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    private Func1<Genre, Observable<DiscoverTvShowsInGenre>> fetchDiscoverTvShows() {
        return new Func1<Genre, Observable<DiscoverTvShowsInGenre>>() {

            @Override
            public Observable<DiscoverTvShowsInGenre> call(final Genre genre) {
                return api.getShowsMatchingGenre(genre.getId()).flatMap(new Func1<DiscoverTvShows, Observable<DiscoverTvShowsInGenre>>() {

                    @Override
                    public Observable<DiscoverTvShowsInGenre> call(DiscoverTvShows discoverTvShows) {
                        return Observable.just(new DiscoverTvShowsInGenre(genre, discoverTvShows));
                    }

                });
            }

        };
    }

    private Observable<Configuration> configurationObservable() {
        return configurationRepository.getConfiguration();
    }

    private static class DiscoverTvShowsInGenre {

        final Genre genre;
        final DiscoverTvShows shows;

        DiscoverTvShowsInGenre(Genre genre, DiscoverTvShows shows) {
            this.genre = genre;
            this.shows = shows;
        }

    }

}
