package com.ataulm.wutson.discover;

import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.repository.persistence.PersistentDataRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonConfiguration;
import com.ataulm.wutson.tmdb.gson.GsonDiscoverTv;
import com.ataulm.wutson.tmdb.gson.GsonGenres;
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

public class ShowsInGenreRepository {

    private final TmdbApi api;
    private final PersistentDataRepository persistentDataRepository;
    private final Gson gson;
    private final ConfigurationRepository configurationRepository;

    private final GenresRepository genresRepository;
    private final BehaviorSubject<List<ShowsInGenre>> subject;

    public ShowsInGenreRepository(TmdbApi api, PersistentDataRepository persistentDataRepository, Gson gson, ConfigurationRepository configurationRepository) {
        this.api = api;
        this.persistentDataRepository = persistentDataRepository;
        this.gson = gson;
        this.configurationRepository = configurationRepository;

        this.genresRepository = new GenresRepository(api, persistentDataRepository, gson);
        this.subject = BehaviorSubject.create();
    }

    public Observable<List<ShowsInGenre>> getShowsSeparatedByGenre() {
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
            public Observable<GsonGenreAndGsonDiscoverTvShows> call(final GsonGenres.Genre genre) {
                return  fetchJsonShowSummariesFrom(persistentDataRepository, genre.id)
                        .flatMap(asGsonDiscoverTv(gson))
                        .switchIfEmpty(api.getShowsMatchingGenre(genre.id).doOnNext(saveTo(persistentDataRepository, gson, genre.id)))
                        .flatMap(asGsonGenreAndGsonDiscoverTvShows(genre));
            }

        };
    }

    private static Action1<? super GsonDiscoverTv> saveTo(final PersistentDataRepository persistentDataRepository, final Gson gson, final String tmdbGenreId) {
        return new Action1<GsonDiscoverTv>() {

            @Override
            public void call(GsonDiscoverTv gsonDiscoverTv) {
                String json = gson.toJson(gsonDiscoverTv, GsonDiscoverTv.class);
                persistentDataRepository.writeJsonShowSummary(tmdbGenreId, json);
            }

        };
    }

    private static Func1<GsonDiscoverTv, Observable<GsonGenreAndGsonDiscoverTvShows>> asGsonGenreAndGsonDiscoverTvShows(final GsonGenres.Genre genre) {
        return new Func1<GsonDiscoverTv, Observable<GsonGenreAndGsonDiscoverTvShows>>() {

            @Override
            public Observable<GsonGenreAndGsonDiscoverTvShows> call(GsonDiscoverTv gsonDiscoverTv) {
                return Observable.just(new GsonGenreAndGsonDiscoverTvShows(genre, gsonDiscoverTv));
            }

        };
    }

    private static Observable<String> fetchJsonShowSummariesFrom(final PersistentDataRepository repository, final String tmdbGenreId) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(repository.readJsonShowSummaries(tmdbGenreId));
                subscriber.onCompleted();
            }

        });
    }

    private static Func1<String, Observable<GsonDiscoverTv>> asGsonDiscoverTv(final Gson gson) {
        return new Func1<String, Observable<GsonDiscoverTv>>() {

            @Override
            public Observable<GsonDiscoverTv> call(final String json) {
                return Observable.create(new Observable.OnSubscribe<GsonDiscoverTv>() {

                    @Override
                    public void call(Subscriber<? super GsonDiscoverTv> subscriber) {
                        if (!json.isEmpty()) {
                            GsonDiscoverTv gsonConfiguration = gson.fromJson(json, GsonDiscoverTv.class);
                            subscriber.onNext(gsonConfiguration);
                        }
                        subscriber.onCompleted();
                    }

                });
            }

        };
    }

    private Observable<GsonConfiguration> repeatingConfigurationObservable() {
        return configurationRepository.getConfiguration().first().repeat();
    }

    private static Func2<GsonConfiguration, GsonGenreAndGsonDiscoverTvShows, ShowsInGenre> combineAsShowsInGenre() {
        return new Func2<GsonConfiguration, GsonGenreAndGsonDiscoverTvShows, ShowsInGenre>() {

            @Override
            public ShowsInGenre call(GsonConfiguration configuration, GsonGenreAndGsonDiscoverTvShows discoverTvShows) {
                GsonGenres.Genre genre = discoverTvShows.genre;
                List<ShowSummary> showSummaries = new ArrayList<>(discoverTvShows.size());
                for (GsonDiscoverTv.Show discoverTvShow : discoverTvShows.gsonDiscoverTv) {
                    String id = discoverTvShow.id;
                    String name = discoverTvShow.name;
                    URI posterUri = URI.create(configuration.getCompletePosterPath(discoverTvShow.posterPath));
                    URI backdropUri = URI.create(configuration.getCompleteBackdropPath(discoverTvShow.backdropPath));

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
