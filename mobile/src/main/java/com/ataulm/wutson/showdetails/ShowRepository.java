package com.ataulm.wutson.showdetails;

import android.util.Log;

import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.repository.persistence.PersistentDataRepository;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonConfiguration;
import com.ataulm.wutson.tmdb.gson.GsonCredits;
import com.ataulm.wutson.tmdb.gson.GsonTvShow;
import com.google.gson.Gson;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class ShowRepository {

    private final TmdbApi api;
    private final PersistentDataRepository persistentDataRepository;
    private final ConfigurationRepository configurationRepository;
    private final Gson gson;

    public ShowRepository(TmdbApi api, PersistentDataRepository persistentDataRepository, ConfigurationRepository configurationRepository, Gson gson) {
        this.api = api;
        this.persistentDataRepository = persistentDataRepository;
        this.configurationRepository = configurationRepository;
        this.gson = gson;
    }

    public Observable<Show> getShowDetails(String showId) {
        Observable<GsonConfiguration> gsonConfigurationObservable = configurationRepository.getConfiguration();
        Observable<GsonTvShow> gsonTvShowObservable = fetchJsonTvShowFrom(persistentDataRepository, showId)
                .flatMap(asGsonTvShow(gson))
                .switchIfEmpty(api.getTvShow(showId).doOnNext(saveTo(persistentDataRepository, gson, showId)));

        return Observable.zip(gsonConfigurationObservable, gsonTvShowObservable, asShow(showId));
    }

    private static Action1<GsonTvShow> saveTo(final PersistentDataRepository persistentDataRepository, final Gson gson, final String tmdbShowId) {
        return new Action1<GsonTvShow>() {

            @Override
            public void call(GsonTvShow gsonTvShow) {
                String json = gson.toJson(gsonTvShow, GsonTvShow.class);
                persistentDataRepository.writeJsonShowDetails(tmdbShowId, json);
            }

        };
    }

    private static Func1<String, Observable<GsonTvShow>> asGsonTvShow(final Gson gson) {
        return new Func1<String, Observable<GsonTvShow>>() {

            @Override
            public Observable<GsonTvShow> call(final String json) {
                return Observable.create(new Observable.OnSubscribe<GsonTvShow>() {

                    @Override
                    public void call(Subscriber<? super GsonTvShow> subscriber) {
                        if (json.isEmpty()) {
                            Log.w("WHATWHAT", "TvShow json is empty");
                        } else {
                            GsonTvShow gsonTvShow = gson.fromJson(json, GsonTvShow.class);
                            subscriber.onNext(gsonTvShow);
                        }
                        subscriber.onCompleted();
                    }

                });
            }

        };
    }

    private static Observable<String> fetchJsonTvShowFrom(final PersistentDataRepository repository, final String showId) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(repository.readJsonShowDetails(showId));
                subscriber.onCompleted();
            }

        });
    }

    private static Func2<GsonConfiguration, GsonTvShow, Show> asShow(final String showId) {
        return new Func2<GsonConfiguration, GsonTvShow, Show>() {

            @Override
            public Show call(GsonConfiguration gsonConfiguration, GsonTvShow gsonTvShow) {
                List<Character> characters = getCharacters(gsonConfiguration, gsonTvShow);

                String name = gsonTvShow.name;
                String overview = gsonTvShow.overview;
                URI posterUri = URI.create(gsonConfiguration.getCompletePosterPath(gsonTvShow.posterPath));
                URI backdropUri = URI.create(gsonConfiguration.getCompletePosterPath(gsonTvShow.backdropPath));
                Cast cast = new Cast(characters);

                List<Show.Season> seasons = getSeasons(gsonConfiguration, gsonTvShow);
                return new Show(gsonTvShow.id, name, overview, posterUri, backdropUri, cast, seasons);
            }

            private List<Character> getCharacters(GsonConfiguration gsonConfiguration, GsonTvShow gsonTvShow) {
                List<Character> characters = new ArrayList<>();
                for (GsonCredits.Cast.Entry entry : gsonTvShow.gsonCredits.cast) {
                    Actor actor = new Actor(entry.actorName, URI.create(gsonConfiguration.getCompleteProfilePath(entry.profilePath)));
                    characters.add(new Character(entry.name, actor));
                }
                return characters;
            }

            private List<Show.Season> getSeasons(GsonConfiguration gsonConfiguration, GsonTvShow gsonTvShow) {
                List<Show.Season> seasons = new ArrayList<>();
                for (GsonTvShow.Season season : gsonTvShow.seasons) {
                    String id = season.id;
                    int seasonNumber = season.seasonNumber;
                    int episodeCount = season.episodeCount;
                    URI posterPath = URI.create(gsonConfiguration.getCompletePosterPath(season.posterPath));
                    seasons.add(new Show.Season(id, showId, seasonNumber, episodeCount, posterPath));
                }
                return seasons;
            }

        };
    }

}
