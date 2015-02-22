package com.ataulm.wutson.repository;

import com.ataulm.wutson.discover.ShowsInGenre;
import com.ataulm.wutson.discover.ShowsInGenreRepository;
import com.ataulm.wutson.tmdb.Character;
import com.ataulm.wutson.tmdb.Configuration;
import com.ataulm.wutson.tmdb.Season;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.TvShow;
import com.ataulm.wutson.show.Actor;
import com.ataulm.wutson.show.Cast;
import com.ataulm.wutson.show.Show;

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
        Observable<Configuration> configurationObservable = configurationRepository.getConfiguration();
        Observable<TvShow> tvShowObservable = configurationObservable.flatMap(getTvShowWith(showId));

        return Observable.zip(configurationObservable, tvShowObservable, new Func2<Configuration, TvShow, Show>() {

            @Override
            public Show call(Configuration configuration, TvShow tvShow) {
                List<com.ataulm.wutson.show.Character> characters = new ArrayList<>();
                for (Character character : tvShow.getCredits()) {
                    Actor actor = new Actor(character.actorName, URI.create(configuration.getCompleteProfilePath(character.profilePath)));
                    characters.add(new com.ataulm.wutson.show.Character(character.name, actor));
                }

                String name = tvShow.getName();
                String overview = tvShow.getOverview();
                URI posterUri = URI.create(tvShow.getPosterPath());
                Cast cast = new Cast(characters);
                List<Season> seasons = tvShow.getSeasons();
                return new Show(name, overview, posterUri, cast, seasons);
            }

        });
    }

    private Func1<Configuration, Observable<TvShow>> getTvShowWith(final String id) {
        return new Func1<Configuration, Observable<TvShow>>() {

            @Override
            public Observable<TvShow> call(final Configuration configuration) {
                Map<String, String> params = new HashMap<>();
                params.put("append_to_response", "credits");
                return api.getTvShow(id).map(updateTvShowWith(configuration));
            }

        };
    }

    private Func1<TvShow, TvShow> updateTvShowWith(final Configuration configuration) {
        return new Func1<TvShow, TvShow>() {

            @Override
            public TvShow call(TvShow tvShow) {
                tvShow.setConfiguration(configuration);
                return tvShow;
            }

        };
    }

}
