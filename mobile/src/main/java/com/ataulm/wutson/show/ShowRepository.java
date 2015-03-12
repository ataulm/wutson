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
                for (GsonCredits.Cast.Entry entry : gsonTvShow.gsonCredits.cast) {
                    Actor actor = new Actor(entry.actorName, URI.create(gsonConfiguration.getCompleteProfilePath(entry.profilePath)));
                    characters.add(new Character(entry.name, actor));
                }

                String name = gsonTvShow.name;
                String overview = gsonTvShow.overview;
                URI posterUri = URI.create(gsonConfiguration.getCompletePosterPath(gsonTvShow.posterPath));
                Cast cast = new Cast(characters);

                List<Show.Season> seasons = new ArrayList<>();
                for (GsonTvShow.Season season : gsonTvShow.seasons) {
                    String id = season.id;
                    int seasonNumber = season.seasonNumber;
                    int episodeCount = season.episodeCount;
                    URI posterPath = URI.create(gsonConfiguration.getCompletePosterPath(season.posterPath));
                    seasons.add(new Show.Season(id, showId, seasonNumber, episodeCount, posterPath));
                }
                return new Show(gsonTvShow.id, name, overview, posterUri, cast, seasons);
            }

        });
    }

}
