package com.ataulm.wutson.core.showdetails;

import com.ataulm.wutson.core.model.TmdbConfiguration;
import com.ataulm.wutson.core.showdetails.Actor;
import com.ataulm.wutson.core.showdetails.Cast;
import com.ataulm.wutson.core.showdetails.Character;
import com.ataulm.wutson.core.showdetails.Show;
import com.ataulm.wutson.core.tmdb.TmdbApi;
import com.ataulm.wutson.core.tmdb.gson.GsonCredits;
import com.ataulm.wutson.core.tmdb.gson.GsonTvShow;
import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.repository.persistence.PersistentDataRepository;
import com.ataulm.wutson.rx.Function;
import com.google.gson.Gson;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func2;

import static com.ataulm.wutson.rx.Function.*;
import static com.ataulm.wutson.rx.Function.ignoreEmptyStrings;
import static com.ataulm.wutson.rx.Function.jsonTo;

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
        Observable<TmdbConfiguration> configurationObservable = configurationRepository.getConfiguration();
        Observable<GsonTvShow> gsonTvShowObservable = fetchJsonTvShowFrom(persistentDataRepository, showId)
                .filter(ignoreEmptyStrings())
                .map(jsonTo(GsonTvShow.class, gson))
                .switchIfEmpty(api.getTvShow(showId).doOnNext(saveTo(persistentDataRepository, gson, showId)));

        return Observable.zip(configurationObservable, gsonTvShowObservable, asShow(showId));
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

    private static Observable<String> fetchJsonTvShowFrom(final PersistentDataRepository repository, final String showId) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(repository.readJsonShowDetails(showId));
                subscriber.onCompleted();
            }

        });
    }

    private static Func2<TmdbConfiguration, GsonTvShow, Show> asShow(final String showId) {
        return new Func2<TmdbConfiguration, GsonTvShow, Show>() {

            @Override
            public Show call(TmdbConfiguration configuration, GsonTvShow gsonTvShow) {
                List<com.ataulm.wutson.core.showdetails.Character> characters = getCharacters(configuration, gsonTvShow);

                String name = gsonTvShow.name;
                String overview = gsonTvShow.overview;
                URI posterUri = configuration.completePoster(gsonTvShow.posterPath);
                URI backdropUri = configuration.completeBackdrop(gsonTvShow.backdropPath);
                Cast cast = new Cast(characters);

                List<Show.SeasonSummary> seasonSummaries = getSeasons(configuration, gsonTvShow);
                return new Show(gsonTvShow.id, name, overview, posterUri, backdropUri, cast, seasonSummaries);
            }

            private List<com.ataulm.wutson.core.showdetails.Character> getCharacters(TmdbConfiguration configuration, GsonTvShow gsonTvShow) {
                List<com.ataulm.wutson.core.showdetails.Character> characters = new ArrayList<>();
                for (GsonCredits.Cast.Entry entry : gsonTvShow.gsonCredits.cast) {
                    Actor actor = new Actor(entry.actorName, configuration.completeProfile(entry.profilePath));
                    characters.add(new Character(entry.name, actor));
                }
                return characters;
            }

            private List<Show.SeasonSummary> getSeasons(TmdbConfiguration configuration, GsonTvShow gsonTvShow) {
                List<Show.SeasonSummary> seasonSummaries = new ArrayList<>();
                for (GsonTvShow.Season season : gsonTvShow.seasons) {
                    String id = season.id;
                    String showName = gsonTvShow.name;
                    int seasonNumber = season.seasonNumber;
                    int episodeCount = season.episodeCount;
                    URI posterPath = configuration.completePoster(season.posterPath);
                    seasonSummaries.add(new Show.SeasonSummary(id, showId, showName, seasonNumber, episodeCount, posterPath));
                }
                return seasonSummaries;
            }

        };
    }

}
