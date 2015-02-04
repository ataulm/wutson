package com.ataulm.wutson.discover;

import com.ataulm.wutson.model.DiscoverTvShows;
import com.ataulm.wutson.model.Genre;
import com.ataulm.wutson.model.Genres;
import com.ataulm.wutson.model.TmdbApi;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class ShowsInGenreRepository {

    private final TmdbApi api;
    private final GenresRepository genresRepository;
    private final BehaviorSubject<List<com.ataulm.wutson.discover.ShowsInGenre>> subject;

    private boolean initialised;

    public ShowsInGenreRepository(TmdbApi api) {
        this.api = api;

        this.genresRepository = new GenresRepository(api);
        this.subject = BehaviorSubject.create();
    }

    public Observable<List<com.ataulm.wutson.discover.ShowsInGenre>> getShowsSeparatedByGenre() {
        if (!initialised) {
            refreshBrowseShows();
        }
        return subject;
    }

    private void refreshBrowseShows() {
        genresRepository.getGenres().map(new Func1<Genres, Observable<Genre>>() {

            @Override
            public Observable<Genre> call(Genres genres) {
                return Observable.from(genres);
            }

        }).flatMap(new Func1<Observable<Genre>, Observable<com.ataulm.wutson.discover.ShowsInGenre>>() {

            @Override
            public Observable<com.ataulm.wutson.discover.ShowsInGenre> call(Observable<Genre> genreObservable) {
                return genreObservable.flatMap(new Func1<Genre, Observable<com.ataulm.wutson.discover.ShowsInGenre>>() {

                    @Override
                    public Observable<com.ataulm.wutson.discover.ShowsInGenre> call(final Genre genre) {
                        Observable<DiscoverTvShows> showsMatchingGenre = api.getShowsMatchingGenre(genre.getId());
                        return showsMatchingGenre.map(new Func1<DiscoverTvShows, com.ataulm.wutson.discover.ShowsInGenre>() {

                            @Override
                            public com.ataulm.wutson.discover.ShowsInGenre call(DiscoverTvShows discoverTvShows) {
                                return com.ataulm.wutson.discover.ShowsInGenre.from(genre, discoverTvShows);
                            }

                        });
                    }

                });
            }

        })
                .toList()
                .doOnNext(markAsInitialised())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    private Action1<List<com.ataulm.wutson.discover.ShowsInGenre>> markAsInitialised() {
        return new Action1<List<com.ataulm.wutson.discover.ShowsInGenre>>() {

            @Override
            public void call(List<com.ataulm.wutson.discover.ShowsInGenre> shows) {
                initialised = true;
            }

        };
    }

}
