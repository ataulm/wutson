package com.ataulm.wutson.discover;

import com.ataulm.wutson.model.DiscoverTvShows;
import com.ataulm.wutson.model.Genre;
import com.ataulm.wutson.model.Genres;
import com.ataulm.wutson.model.TmdbApi;
import com.ataulm.wutson.repository.InfiniteOperator;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class ShowsInGenreRepository {

    private final TmdbApi api;
    private final GenresRepository genresRepository;
    private final BehaviorSubject<List<ShowsInGenre>> subject;

    public ShowsInGenreRepository(TmdbApi api) {
        this.api = api;

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
        genresRepository.getGenres().map(new Func1<Genres, Observable<Genre>>() {

            @Override
            public Observable<Genre> call(Genres genres) {
                return Observable.from(genres);
            }

        }).flatMap(new Func1<Observable<Genre>, Observable<ShowsInGenre>>() {

            @Override
            public Observable<ShowsInGenre> call(Observable<Genre> genreObservable) {
                return genreObservable.flatMap(new Func1<Genre, Observable<ShowsInGenre>>() {

                    @Override
                    public Observable<ShowsInGenre> call(final Genre genre) {
                        Observable<DiscoverTvShows> showsMatchingGenre = api.getShowsMatchingGenre(genre.getId());
                        return showsMatchingGenre.map(new Func1<DiscoverTvShows, ShowsInGenre>() {

                            @Override
                            public ShowsInGenre call(DiscoverTvShows discoverTvShows) {
                                return ShowsInGenre.from(genre, discoverTvShows);
                            }

                        });
                    }

                });
            }

        })
                .toList()
                .lift(new InfiniteOperator<List<ShowsInGenre>>())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

}
