package com.ataulm.wutson.shows.discover;

import com.ataulm.wutson.repository.persistence.LocalDataRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.tmdb.Configuration;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonDiscoverTv;
import com.ataulm.wutson.tmdb.gson.GsonGenres;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import static com.ataulm.wutson.rx.Function.ignoreEmptyStrings;
import static com.ataulm.wutson.rx.Function.jsonTo;

public class DiscoverShowsRepository {

    private final TmdbApi api;
    private final LocalDataRepository localDataRepository;
    private final Gson gson;

    private final GenresRepository genresRepository;
    private final BehaviorSubject<List<ShowsInGenre>> subject;

    public DiscoverShowsRepository(TmdbApi api, LocalDataRepository localDataRepository, Gson gson) {
        this.api = api;
        this.localDataRepository = localDataRepository;
        this.gson = gson;

        this.genresRepository = new GenresRepository(api, localDataRepository, gson);
        this.subject = BehaviorSubject.create();
    }

    public Observable<List<ShowsInGenre>> getShowsInGenre() {
        if (!subject.hasValue()) {
            // TODO: fetch configuration
            Observable<Configuration> configuration = Observable.empty();
            Observable<GsonDiscoverTvShows> gsonDiscoverTvShows =
                    Observable.concat(gsonDiscoverTvShowsFromDisk(), gsonDiscoverTvShowsFromNetwork())
                            .first();

            Observable.zip(configuration, gsonDiscoverTvShows, asShowsInGenreList())
                    .lift(Function.<List<ShowsInGenre>>swallowOnCompleteEvents())
                    .subscribeOn(Schedulers.io())
                    .subscribe(subject);
        }
        return subject;
    }

    private static Func2<Configuration, GsonDiscoverTvShows, List<ShowsInGenre>> asShowsInGenreList() {
        return new Func2<Configuration, GsonDiscoverTvShows, List<ShowsInGenre>>() {
            @Override
            public List<ShowsInGenre> call(Configuration configuration, GsonDiscoverTvShows gsonDiscoverTvShows) {
                // TODO: convert to ShowsInGenre
                return Collections.emptyList();
            }
        };
    }

    private Observable<GsonDiscoverTvShows> gsonDiscoverTvShowsFromDisk() {
        return Observable.empty();
    }

    private Observable<GsonDiscoverTvShows> gsonDiscoverTvShowsFromNetwork() {
        // TODO: persist to disk
        return genresRepository.getGenres()
                .flatMap(Function.<GsonGenres.Genre>emitEachElement())
                .flatMap(fetchDiscoverTvShows())
                .toList().map(new Func1<List<Pair<GsonGenres.Genre, GsonDiscoverTv>>, GsonDiscoverTvShows>() {
                    @Override
                    public GsonDiscoverTvShows call(List<Pair<GsonGenres.Genre, GsonDiscoverTv>> pairs) {
                        GsonGenres.Genre genre = pairs.get(0).a;
                        List<GsonDiscoverTv> discoverShows = Observable.from(pairs)
                                .map(new Func1<Pair<GsonGenres.Genre, GsonDiscoverTv>, GsonDiscoverTv>() {
                                    @Override
                                    public GsonDiscoverTv call(Pair<GsonGenres.Genre, GsonDiscoverTv> genreGsonDiscoverTvPair) {
                                        return genreGsonDiscoverTvPair.b;
                                    }
                                })
                                .toList()
                                .toBlocking()
                                .first();

                        return new GsonDiscoverTvShows(genre, discoverShows);
                    }
                });
    }

    private Func1<GsonGenres.Genre, Observable<Pair<GsonGenres.Genre, GsonDiscoverTv>>> fetchDiscoverTvShows() {
        return new Func1<GsonGenres.Genre, Observable<Pair<GsonGenres.Genre, GsonDiscoverTv>>>() {

            @Override
            public Observable<Pair<GsonGenres.Genre, GsonDiscoverTv>> call(GsonGenres.Genre genre) {
                return fetchJsonShowSummariesFrom(localDataRepository, genre.id)
                        .filter(ignoreEmptyStrings())
                        .map(jsonTo(GsonDiscoverTv.class, gson))
                        .switchIfEmpty(api.getShowsMatchingGenre(genre.id).doOnNext(saveTo(localDataRepository, gson, genre.id)))
                        .map(asGsonGenreAndGsonDiscoverTvShows(genre));
            }

        };
    }

    private static Observable<String> fetchJsonShowSummariesFrom(final LocalDataRepository repository, final String tmdbGenreId) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                String json = repository.readJsonShowSummaries(tmdbGenreId);
                subscriber.onNext(json);
                subscriber.onCompleted();
            }

        });
    }

    private static Action1<? super GsonDiscoverTv> saveTo(final LocalDataRepository localDataRepository, final Gson gson, final String tmdbGenreId) {
        return new Action1<GsonDiscoverTv>() {

            @Override
            public void call(GsonDiscoverTv gsonDiscoverTv) {
                String json = gson.toJson(gsonDiscoverTv, GsonDiscoverTv.class);
                localDataRepository.writeJsonShowSummary(tmdbGenreId, json);
            }

        };
    }

    private static Func1<GsonDiscoverTv, Pair<GsonGenres.Genre, GsonDiscoverTv>> asGsonGenreAndGsonDiscoverTvShows(final GsonGenres.Genre genre) {
        return new Func1<GsonDiscoverTv, Pair<GsonGenres.Genre, GsonDiscoverTv>>() {

            @Override
            public Pair<GsonGenres.Genre, GsonDiscoverTv> call(GsonDiscoverTv gsonDiscoverTv) {
                return new Pair<>(genre, gsonDiscoverTv);
            }

        };
    }

}
