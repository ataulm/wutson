package com.ataulm.wutson.repository;

import com.ataulm.wutson.model.ShowId;
import com.ataulm.wutson.repository.persistence.LocalDataRepository;
import com.ataulm.wutson.tmdb.Configuration;
import com.ataulm.wutson.model.Genre;
import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.model.ShowsInGenre;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonDiscoverTv;
import com.ataulm.wutson.tmdb.gson.GsonGenres;
import com.ataulm.wutson.rx.Function;
import com.google.gson.Gson;

import java.net.URI;
import java.util.ArrayList;
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

public class ShowsInGenreRepository {

    private final TmdbApi api;
    private final LocalDataRepository localDataRepository;
    private final ConfigurationRepository configurationRepository;
    private final GenresRepository genresRepository;
    private final Gson gson;

    private final BehaviorSubject<List<ShowsInGenre>> subject;

    public ShowsInGenreRepository(TmdbApi api, LocalDataRepository localDataRepository, ConfigurationRepository configurationRepository, GenresRepository genresRepository, Gson gson) {
        this.api = api;
        this.localDataRepository = localDataRepository;
        this.configurationRepository = configurationRepository;
        this.genresRepository = genresRepository;
        this.gson = gson;

        this.subject = BehaviorSubject.create();
    }

    public Observable<List<ShowsInGenre>> getDiscoverShowsList() {
        if (!subject.hasValue()) {
            refreshBrowseShows();
        }
        return subject;
    }

    private void refreshBrowseShows() {
        Observable<GsonGenres.Genre> genreObservable = genresRepository.getGenres().flatMap(Function.<GsonGenres.Genre>emitEachElement());
        Observable<GsonGenreAndGsonDiscoverTvShows> discoverTvShowsObservable = genreObservable.flatMap(fetchDiscoverTvShows());
        Observable<ShowsInGenre> showsInGenreObservable = Observable.zip(repeatingConfigurationObservable(), discoverTvShowsObservable, combineAsShowsInGenre());

        showsInGenreObservable.toList()
                .lift(Function.<List<ShowsInGenre>>swallowOnCompleteEvents())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    private Func1<GsonGenres.Genre, Observable<GsonGenreAndGsonDiscoverTvShows>> fetchDiscoverTvShows() {
        return new Func1<GsonGenres.Genre, Observable<GsonGenreAndGsonDiscoverTvShows>>() {

            @Override
            public Observable<GsonGenreAndGsonDiscoverTvShows> call(GsonGenres.Genre genre) {
                return fetchJsonShowSummariesFrom(localDataRepository, genre.id)
                        .filter(ignoreEmptyStrings())
                        .map(jsonTo(GsonDiscoverTv.class, gson))
                        .switchIfEmpty(api.getShowsMatchingGenre(genre.id).doOnNext(saveTo(localDataRepository, gson, genre.id)))
                        .map(asGsonGenreAndGsonDiscoverTvShows(genre));
            }

        };
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

    private static Func1<GsonDiscoverTv, GsonGenreAndGsonDiscoverTvShows> asGsonGenreAndGsonDiscoverTvShows(final GsonGenres.Genre genre) {
        return new Func1<GsonDiscoverTv, GsonGenreAndGsonDiscoverTvShows>() {

            @Override
            public GsonGenreAndGsonDiscoverTvShows call(GsonDiscoverTv gsonDiscoverTv) {
                return new GsonGenreAndGsonDiscoverTvShows(genre, gsonDiscoverTv);
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

    private Observable<Configuration> repeatingConfigurationObservable() {
        return configurationRepository.getConfiguration().first().repeat();
    }

    private static Func2<Configuration, GsonGenreAndGsonDiscoverTvShows, ShowsInGenre> combineAsShowsInGenre() {
        return new Func2<Configuration, GsonGenreAndGsonDiscoverTvShows, ShowsInGenre>() {

            @Override
            public ShowsInGenre call(Configuration configuration, GsonGenreAndGsonDiscoverTvShows discoverTvShows) {
                Genre genre = new Genre(discoverTvShows.genre.id, discoverTvShows.genre.name);
                List<ShowSummary> showSummaries = new ArrayList<>(discoverTvShows.size());
                for (GsonDiscoverTv.Show discoverTvShow : discoverTvShows.gsonDiscoverTv) {
                    ShowId id = new ShowId(discoverTvShow.id);
                    String name = discoverTvShow.name;
                    URI posterUri = configuration.completePoster(discoverTvShow.posterPath);
                    URI backdropUri = configuration.completeBackdrop(discoverTvShow.backdropPath);

                    showSummaries.add(new ShowSummary(id, name, posterUri, backdropUri));
                }
                return new ShowsInGenre(genre, showSummaries);
            }

        };
    }

    private static class GsonGenreAndGsonDiscoverTvShows {

        final GsonGenres.Genre genre;
        final GsonDiscoverTv gsonDiscoverTv;

        GsonGenreAndGsonDiscoverTvShows(GsonGenres.Genre genre, GsonDiscoverTv gsonDiscoverTv) {
            this.genre = genre;
            this.gsonDiscoverTv = gsonDiscoverTv;
        }

        int size() {
            return gsonDiscoverTv.shows.size();
        }

    }

}
