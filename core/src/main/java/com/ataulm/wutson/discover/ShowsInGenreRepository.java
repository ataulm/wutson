package com.ataulm.wutson.discover;

import com.ataulm.wutson.tmdb.Configuration;
import com.ataulm.wutson.tmdb.DiscoverTvShows;
import com.ataulm.wutson.tmdb.GsonGenre;
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
        Observable<GsonGenre> genreObservable = genresRepository.getGenres().flatMap(Functions.<GsonGenre>iterate());
        Observable<DiscoverTvShowsInGenre> discoverTvShowsObservable = genreObservable.flatMap(fetchDiscoverTvShows());

        Observable<ShowsInGenre> showsInGenreObservable = Observable.combineLatest(configurationObservable(), discoverTvShowsObservable, new Func2<Configuration, DiscoverTvShowsInGenre, ShowsInGenre>() {

            @Override
            public ShowsInGenre call(Configuration configuration, DiscoverTvShowsInGenre discoverTvShows) {
                GsonGenre gsonGenre = discoverTvShows.gsonGenre;
                List<Show> shows = new ArrayList<>(discoverTvShows.shows.size());
                for (DiscoverTvShows.Show discoverTvShow : discoverTvShows.shows) {
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

    private Func1<GsonGenre, Observable<DiscoverTvShowsInGenre>> fetchDiscoverTvShows() {
        return new Func1<GsonGenre, Observable<DiscoverTvShowsInGenre>>() {

            @Override
            public Observable<DiscoverTvShowsInGenre> call(final GsonGenre gsonGenre) {
                return api.getShowsMatchingGenre(gsonGenre.getId()).flatMap(new Func1<DiscoverTvShows, Observable<DiscoverTvShowsInGenre>>() {

                    @Override
                    public Observable<DiscoverTvShowsInGenre> call(DiscoverTvShows discoverTvShows) {
                        return Observable.just(new DiscoverTvShowsInGenre(gsonGenre, discoverTvShows));
                    }

                });
            }

        };
    }

    private Observable<Configuration> configurationObservable() {
        return configurationRepository.getConfiguration().first();
    }

    private static class DiscoverTvShowsInGenre {

        final GsonGenre gsonGenre;
        final DiscoverTvShows shows;

        DiscoverTvShowsInGenre(GsonGenre gsonGenre, DiscoverTvShows shows) {
            this.gsonGenre = gsonGenre;
            this.shows = shows;
        }

    }

}
