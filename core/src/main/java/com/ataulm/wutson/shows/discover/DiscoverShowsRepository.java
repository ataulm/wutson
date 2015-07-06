package com.ataulm.wutson.shows.discover;

import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.repository.persistence.LocalDataRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.shows.Genre;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.ShowSummaries;
import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.tmdb.Configuration;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonDiscoverTv;
import com.ataulm.wutson.tmdb.gson.GsonGenres;
import com.google.gson.Gson;

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
    private final ConfigurationRepository configurationRepository;
    private final LocalDataRepository localDataRepository;
    private final Gson gson;

    private final GenresRepository genresRepository;
    private final BehaviorSubject<List<ShowsInGenre>> subject;

    public DiscoverShowsRepository(TmdbApi api, ConfigurationRepository configurationRepository, LocalDataRepository localDataRepository, Gson gson) {
        this.api = api;
        this.configurationRepository = configurationRepository;
        this.localDataRepository = localDataRepository;
        this.gson = gson;

        this.genresRepository = new GenresRepository(api, localDataRepository, gson);
        this.subject = BehaviorSubject.create();
    }

    public Observable<List<ShowsInGenre>> getListOfShowsInGenre() {
        if (!subject.hasValue()) {
            refreshListOfShowsInGenre();
        }
        return subject;
    }

    private void refreshListOfShowsInGenre() {
        Observable<List<GsonShowsInGenre>> gsonShowsInGenre = getGsonShowsInGenre();
        Observable<Configuration> configuration = configurationRepository.getConfiguration();

        Observable.combineLatest(gsonShowsInGenre, configuration, asListOfShowsInGenre())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    private Observable<List<GsonShowsInGenre>> getGsonShowsInGenre() {
        return Observable
                .concat(gsonDiscoverTvShowsFromDisk(), gsonDiscoverTvShowsFromNetwork())
                .first();
    }

    private Observable<List<GsonShowsInGenre>> gsonDiscoverTvShowsFromDisk() {
        // TODO: read from disk
        return Observable.empty();
    }

    private Observable<List<GsonShowsInGenre>> gsonDiscoverTvShowsFromNetwork() {
        return genresRepository.getGenres()
                .flatMap(Function.<GsonGenres.Genre>emitEachElement())
                .flatMap(fetchGsonShowsInGenre())
                .doOnNext(new Action1<GsonShowsInGenre>() {
                    @Override
                    public void call(GsonShowsInGenre gsonShowsInGenre) {
                        // TODO: persist to disk
                    }
                })
                .toList();
    }

    private Func1<GsonGenres.Genre, Observable<GsonShowsInGenre>> fetchGsonShowsInGenre() {
        return new Func1<GsonGenres.Genre, Observable<GsonShowsInGenre>>() {

            @Override
            public Observable<GsonShowsInGenre> call(GsonGenres.Genre genre) {
                return fetchJsonShowSummariesFrom(localDataRepository, genre.id)
                        .filter(ignoreEmptyStrings())
                        .map(jsonTo(GsonDiscoverTv.class, gson))
                        .switchIfEmpty(api.getShowsMatchingGenre(genre.id).doOnNext(saveTo(localDataRepository, gson, genre.id)))
                        .map(asGsonDiscoverTvByGenre(genre));
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

    private static Func1<GsonDiscoverTv, GsonShowsInGenre> asGsonDiscoverTvByGenre(final GsonGenres.Genre genre) {
        return new Func1<GsonDiscoverTv, GsonShowsInGenre>() {

            @Override
            public GsonShowsInGenre call(GsonDiscoverTv gsonDiscoverTv) {
                return new GsonShowsInGenre(genre, gsonDiscoverTv);
            }

        };
    }

    private static Func2<List<GsonShowsInGenre>, Configuration, List<ShowsInGenre>> asListOfShowsInGenre() {
        return new Func2<List<GsonShowsInGenre>, Configuration, List<ShowsInGenre>>() {

            @Override
            public List<ShowsInGenre> call(List<GsonShowsInGenre> shows, Configuration configuration) {
                return Observable.from(shows)
                        .flatMap(createShowsInGenreByMergingWith(configuration))
                        .toList().toBlocking().first();
            }

        };
    }

    private static Func1<GsonShowsInGenre, Observable<ShowsInGenre>> createShowsInGenreByMergingWith(final Configuration configuration) {
        return new Func1<GsonShowsInGenre, Observable<ShowsInGenre>>() {
            @Override
            public Observable<ShowsInGenre> call(GsonShowsInGenre gsonShowsInGenre) {
                Genre genre = new Genre(gsonShowsInGenre.getGenre().id, gsonShowsInGenre.getGenre().name);
                return Observable.from(gsonShowsInGenre.getShows())
                        .map(asShowSummary(configuration))
                        .toList()
                        .map(asShowSummaries())
                        .map(asShowsIn(genre));
            }
        };
    }

    private static Func1<GsonDiscoverTv.Show, ShowSummary> asShowSummary(final Configuration configuration) {
        return new Func1<GsonDiscoverTv.Show, ShowSummary>() {
            @Override
            public ShowSummary call(GsonDiscoverTv.Show show) {
                return new ShowSummary(new ShowId(show.id),
                        show.name,
                        configuration.completePoster(show.posterPath),
                        configuration.completeBackdrop(show.backdropPath));
            }
        };
    }

    private static Func1<List<ShowSummary>, ShowSummaries> asShowSummaries() {
        return new Func1<List<ShowSummary>, ShowSummaries>() {
            @Override
            public ShowSummaries call(List<ShowSummary> showSummaries) {
                return new ShowSummaries(showSummaries);
            }
        };
    }

    private static Func1<ShowSummaries, ShowsInGenre> asShowsIn(final Genre genre) {
        return new Func1<ShowSummaries, ShowsInGenre>() {
            @Override
            public ShowsInGenre call(ShowSummaries showSummaries) {
                return new ShowsInGenre(genre, showSummaries);
            }
        };
    }

}
