package com.ataulm.wutson.repository;

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

class BrowseShowsRepository {

    private final TmdbApi api;
    private final GenresRepository genresRepository;
    private final BehaviorSubject<List<DiscoverTvShows>> subject;

    private boolean initialised;

    BrowseShowsRepository(TmdbApi api) {
        this.api = api;

        this.genresRepository = new GenresRepository(api);
        this.subject = BehaviorSubject.create();
    }

    Observable<List<DiscoverTvShows>> getBrowseShows() {
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

        })
                .toList()
                .doOnNext(markAsInitialised())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    private Action1<List<DiscoverTvShows>> markAsInitialised() {
        return new Action1<List<DiscoverTvShows>>() {

            @Override
            public void call(List<DiscoverTvShows> shows) {
                initialised = true;
            }

        };
    }

}
