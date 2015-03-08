package com.ataulm.wutson.discover;

import com.ataulm.wutson.tmdb.GsonConfiguration;
import com.ataulm.wutson.tmdb.GsonDiscoverTvShows;
import com.ataulm.wutson.tmdb.GsonGenres;
import com.ataulm.wutson.tmdb.TmdbApi;
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
        Observable<GsonGenres.GsonGenre> genreObservable = genresRepository.getGenres().flatMap(Functions.<GsonGenres.GsonGenre>iterate());
        Observable<GsonGenreAndGsonDiscoverTvShows> discoverTvShowsObservable = genreObservable.flatMap(fetchDiscoverTvShows());

        Observable<ShowsInGenre> showsInGenreObservable = Observable.combineLatest(configurationObservable(), discoverTvShowsObservable, new Func2<GsonConfiguration, GsonGenreAndGsonDiscoverTvShows, ShowsInGenre>() {

            @Override
            public ShowsInGenre call(GsonConfiguration configuration, GsonGenreAndGsonDiscoverTvShows discoverTvShows) {
                GsonGenres.GsonGenre gsonGenre = discoverTvShows.gsonGenre;
                List<Show> shows = new ArrayList<>(discoverTvShows.size());
                for (GsonDiscoverTvShows.Show discoverTvShow : discoverTvShows.gsonDiscoverTvShows) {
                    String id = discoverTvShow.id;
                    String name = discoverTvShow.name;
                    URI posterUri = URI.create(configuration.getCompletePosterPath(discoverTvShow.posterPath));

                    shows.add(new Show(id, name, posterUri));
                }
                ShowsInGenre showsInGenre = new ShowsInGenre(gsonGenre, shows);
                return showsInGenre;
            }

        });

        showsInGenreObservable.toList()
                .lift(new InfiniteOperator<List<ShowsInGenre>>())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    private Func1<GsonGenres.GsonGenre, Observable<GsonGenreAndGsonDiscoverTvShows>> fetchDiscoverTvShows() {
        return new Func1<GsonGenres.GsonGenre, Observable<GsonGenreAndGsonDiscoverTvShows>>() {

            @Override
            public Observable<GsonGenreAndGsonDiscoverTvShows> call(final GsonGenres.GsonGenre gsonGenre) {
                return api.getShowsMatchingGenre(gsonGenre.id).flatMap(new Func1<GsonDiscoverTvShows, Observable<GsonGenreAndGsonDiscoverTvShows>>() {

                    @Override
                    public Observable<GsonGenreAndGsonDiscoverTvShows> call(GsonDiscoverTvShows gsonDiscoverTvShows) {
                        return Observable.just(new GsonGenreAndGsonDiscoverTvShows(gsonGenre, gsonDiscoverTvShows));
                    }

                });
            }

        };
    }

    private Observable<GsonConfiguration> configurationObservable() {
        return configurationRepository.getConfiguration().first();
    }

    private static class GsonGenreAndGsonDiscoverTvShows {

        final GsonGenres.GsonGenre gsonGenre;
        final GsonDiscoverTvShows gsonDiscoverTvShows;

        GsonGenreAndGsonDiscoverTvShows(GsonGenres.GsonGenre gsonGenre, GsonDiscoverTvShows gsonDiscoverTvShows) {
            this.gsonGenre = gsonGenre;
            this.gsonDiscoverTvShows = gsonDiscoverTvShows;
        }

        int size() {
            return gsonDiscoverTvShows.shows.size();
        }

    }

}
