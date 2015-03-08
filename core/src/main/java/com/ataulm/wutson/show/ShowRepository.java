package com.ataulm.wutson.show;

import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonConfiguration;
import com.ataulm.wutson.tmdb.gson.GsonCredits;
import com.ataulm.wutson.tmdb.gson.GsonTvShow;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class ShowRepository {

    private final TmdbApi api;
    private final ConfigurationRepository configurationRepository;

    public ShowRepository(TmdbApi api, ConfigurationRepository configurationRepository) {
        this.api = api;
        this.configurationRepository = configurationRepository;
    }

    public Observable<Show> getShow(final String showId) {
        Observable<GsonConfiguration> configurationObservable = configurationRepository.getConfiguration();
        Observable<GsonTvShowAndGsonConfiguration> tvShowObservable = configurationObservable.flatMap(getTvShowWith(showId));

        return Observable.zip(configurationObservable, tvShowObservable, new Func2<GsonConfiguration, GsonTvShowAndGsonConfiguration, Show>() {

            @Override
            public Show call(GsonConfiguration gsonConfiguration, GsonTvShowAndGsonConfiguration gsonTvShowAndGsonConfiguration) {
                List<Character> characters = new ArrayList<>();
                for (GsonCredits.GsonCastElement gsonCastElement : gsonTvShowAndGsonConfiguration.gsonTvShow.gsonCredits.gsonCastElements) {
                    Actor actor = new Actor(gsonCastElement.actorName, URI.create(gsonConfiguration.getCompleteProfilePath(gsonCastElement.profilePath)));
                    characters.add(new com.ataulm.wutson.show.Character(gsonCastElement.name, actor));
                }

                String name = gsonTvShowAndGsonConfiguration.gsonTvShow.name;
                String overview = gsonTvShowAndGsonConfiguration.gsonTvShow.overview;
                URI posterUri = gsonTvShowAndGsonConfiguration.createTvPosterPath();
                Cast cast = new Cast(characters);

                List<Show.Season> seasons = new ArrayList<>();
                for (GsonTvShow.GsonSeason gsonSeason : gsonTvShowAndGsonConfiguration.gsonTvShow.gsonSeasons) {
                    String id = gsonSeason.id;
                    int seasonNumber = gsonSeason.seasonNumber;
                    int episodeCount = gsonSeason.episodeCount;
                    URI posterPath = URI.create(gsonConfiguration.getCompletePosterPath(gsonSeason.posterPath));
                    seasons.add(new Show.Season(id, seasonNumber, episodeCount, posterPath));
                }
                return new Show(name, overview, posterUri, cast, seasons);
            }

        });
    }

    private Func1<GsonConfiguration, Observable<GsonTvShowAndGsonConfiguration>> getTvShowWith(final String id) {
        return new Func1<GsonConfiguration, Observable<GsonTvShowAndGsonConfiguration>>() {

            @Override
            public Observable<GsonTvShowAndGsonConfiguration> call(final GsonConfiguration gsonConfiguration) {
                Map<String, String> params = new HashMap<>();
                params.put("append_to_response", "credits");
                return api.getTvShow(id).map(updateTvShowWith(gsonConfiguration));
            }

        };
    }

    private Func1<GsonTvShow, GsonTvShowAndGsonConfiguration> updateTvShowWith(final GsonConfiguration gsonConfiguration) {
        return new Func1<GsonTvShow, GsonTvShowAndGsonConfiguration>() {

            @Override
            public GsonTvShowAndGsonConfiguration call(GsonTvShow gsonTvShow) {
                return new GsonTvShowAndGsonConfiguration(gsonTvShow, gsonConfiguration);
            }

        };
    }

    private static class GsonTvShowAndGsonConfiguration {

        final GsonTvShow gsonTvShow;
        final GsonConfiguration gsonConfiguration;

        GsonTvShowAndGsonConfiguration(GsonTvShow gsonTvShow, GsonConfiguration gsonConfiguration) {
            this.gsonTvShow = gsonTvShow;
            this.gsonConfiguration = gsonConfiguration;
        }

        URI createTvPosterPath() {
            return URI.create(gsonConfiguration.getCompletePosterPath(gsonTvShow.posterPath));
        }

    }

}
