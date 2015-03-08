package com.ataulm.wutson.show;

import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonConfiguration;
import com.ataulm.wutson.tmdb.gson.GsonCredits;
import com.ataulm.wutson.tmdb.gson.GsonTvShow;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
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
        Observable<GsonTvShow> tvShowObservable = api.getTvShow(showId);

        return Observable.zip(configurationObservable, tvShowObservable, new Func2<GsonConfiguration, GsonTvShow, Show>() {

            @Override
            public Show call(GsonConfiguration gsonConfiguration, GsonTvShow gsonTvShow) {
                List<Character> characters = new ArrayList<>();
                for (GsonCredits.GsonCast.GsonCastElement gsonCastElement : gsonTvShow.gsonCredits.gsonCast) {
                    Actor actor = new Actor(gsonCastElement.actorName, URI.create(gsonConfiguration.getCompleteProfilePath(gsonCastElement.profilePath)));
                    characters.add(new com.ataulm.wutson.show.Character(gsonCastElement.name, actor));
                }

                String name = gsonTvShow.name;
                String overview = gsonTvShow.overview;
                URI posterUri = URI.create(gsonConfiguration.getCompletePosterPath(gsonTvShow.posterPath));
                Cast cast = new Cast(characters);

                List<Show.Season> seasons = new ArrayList<>();
                for (GsonTvShow.GsonSeason gsonSeason : gsonTvShow.gsonSeasons) {
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

}
