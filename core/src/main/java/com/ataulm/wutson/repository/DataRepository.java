package com.ataulm.wutson.repository;

import com.ataulm.wutson.discover.ShowsInGenre;
import com.ataulm.wutson.discover.ShowsInGenreRepository;
import com.ataulm.wutson.show.Actor;
import com.ataulm.wutson.show.Cast;
import com.ataulm.wutson.show.Show;
import com.ataulm.wutson.tmdb.GsonConfiguration;
import com.ataulm.wutson.tmdb.GsonCredits;
import com.ataulm.wutson.tmdb.Season;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.TvShow;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class DataRepository {

    private final TmdbApi api;
    private final ConfigurationRepository configurationRepository;
    private final ShowsInGenreRepository showsInGenreRepository;

    public DataRepository(TmdbApi api) {
        this.api = api;

        this.configurationRepository = new ConfigurationRepository(api);
        this.showsInGenreRepository = new ShowsInGenreRepository(api, configurationRepository);
    }

    public Observable<List<ShowsInGenre>> getShowsSeparatedByGenre() {
        return showsInGenreRepository.getShowsSeparatedByGenre();
    }

    public Observable<Show> getShow(final String showId) {
        Observable<GsonConfiguration> configurationObservable = configurationRepository.getConfiguration();
        Observable<TvShow> tvShowObservable = configurationObservable.flatMap(getTvShowWith(showId));

        return Observable.zip(configurationObservable, tvShowObservable, new Func2<GsonConfiguration, TvShow, Show>() {

            @Override
            public Show call(GsonConfiguration gsonConfiguration, TvShow tvShow) {
                List<com.ataulm.wutson.show.Character> characters = new ArrayList<>();
                for (GsonCredits.GsonCastElement gsonCastElement : tvShow.getGsonCredits().gsonCastElements) {
                    Actor actor = new Actor(gsonCastElement.actorName, URI.create(gsonConfiguration.getCompleteProfilePath(gsonCastElement.profilePath)));
                    characters.add(new com.ataulm.wutson.show.Character(gsonCastElement.name, actor));
                }

                String name = tvShow.getName();
                String overview = tvShow.getOverview();
                URI posterUri = URI.create(tvShow.getPosterPath());
                Cast cast = new Cast(characters);

                List<Show.Season> seasons = new ArrayList<>();
                for (Season season : tvShow.getSeasons()) {
                    String id = season.id;
                    int seasonNumber = season.seasonNumber;
                    int episodeCount = season.episodeCount;
                    URI posterPath = URI.create(gsonConfiguration.getCompletePosterPath(season.posterPath));
                    seasons.add(new Show.Season(id, seasonNumber, episodeCount, posterPath));
                }
                return new Show(name, overview, posterUri, cast, seasons);
            }

        });
    }

    private Func1<GsonConfiguration, Observable<TvShow>> getTvShowWith(final String id) {
        return new Func1<GsonConfiguration, Observable<TvShow>>() {

            @Override
            public Observable<TvShow> call(final GsonConfiguration gsonConfiguration) {
                Map<String, String> params = new HashMap<>();
                params.put("append_to_response", "credits");
                return api.getTvShow(id).map(updateTvShowWith(gsonConfiguration));
            }

        };
    }

    private Func1<TvShow, TvShow> updateTvShowWith(final GsonConfiguration gsonConfiguration) {
        return new Func1<TvShow, TvShow>() {

            @Override
            public TvShow call(TvShow tvShow) {
                tvShow.setGsonConfiguration(gsonConfiguration);
                return tvShow;
            }

        };
    }

}
