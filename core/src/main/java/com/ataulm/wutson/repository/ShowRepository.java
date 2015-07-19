package com.ataulm.wutson.repository;

import com.ataulm.wutson.repository.persistence.LocalDataRepository;
import com.ataulm.wutson.shows.Cast;
import com.ataulm.wutson.shows.Character;
import com.ataulm.wutson.shows.Show;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.tmdb.Configuration;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonCredits;
import com.ataulm.wutson.tmdb.gson.GsonTvShow;
import com.ataulm.wutson.trakt.GsonShowDetails;
import com.ataulm.wutson.trakt.GsonShowSeason;
import com.ataulm.wutson.trakt.TraktApi;
import com.google.gson.Gson;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func2;

public class ShowRepository {

    private final TraktApi traktApi;
    private final TmdbApi api;
    private final LocalDataRepository localDataRepository;
    private final ConfigurationRepository configurationRepository;
    private final Gson gson;

    public ShowRepository(TraktApi traktApi, TmdbApi api, LocalDataRepository localDataRepository, ConfigurationRepository configurationRepository, Gson gson) {
        this.traktApi = traktApi;
        this.api = api;
        this.localDataRepository = localDataRepository;
        this.configurationRepository = configurationRepository;
        this.gson = gson;
    }

    public Observable<Show> getShowDetails(ShowId showId) {
        Observable<GsonShowDetails> gsonShowDetailsObservable = traktApi.getShowDetails(showId.toString());
        Observable<List<GsonShowSeason>> gsonShowSeasonsObservable = traktApi.getShowSeasons(showId.toString());

        // TODO: persist!
        return Observable.zip(gsonShowDetailsObservable, gsonShowSeasonsObservable, asShow());
    }

    private static Func2<GsonShowDetails, List<GsonShowSeason>, Show> asShow() {
        return new Func2<GsonShowDetails, List<GsonShowSeason>, Show>() {
            @Override
            public Show call(GsonShowDetails gsonShowDetails, List<GsonShowSeason> gsonShowSeasonList) {
                ShowId id = new ShowId(gsonShowDetails.ids.trakt);
                String title = gsonShowDetails.title;

                URI posterUri = URI.create(gsonShowDetails.images.poster.thumb);
                URI backdropUri = URI.create(gsonShowDetails.images.poster.medium);
                List<Show.SeasonSummary> seasonSummaries = new ArrayList<>(gsonShowSeasonList.size());
                for (GsonShowSeason gsonShowSeason : gsonShowSeasonList) {
                    Show.SeasonSummary seasonSummary = new Show.SeasonSummary(
                            gsonShowSeason.ids.trakt,
                            id,
                            title,
                            gsonShowSeason.number,
                            gsonShowSeason.episodeCount,
                            URI.create(gsonShowDetails.images.poster.medium));
                    seasonSummaries.add(seasonSummary);
                }

                return new Show(
                        id,
                        title,
                        gsonShowDetails.overview,
                        posterUri,
                        backdropUri,
                        new Cast(Collections.<Character>emptyList()),
                        seasonSummaries
                );
            }
        };
    }

    private static Action1<GsonTvShow> saveTo(final LocalDataRepository localDataRepository, final Gson gson, final com.ataulm.wutson.shows.ShowId tmdbShowId) {
        return new Action1<GsonTvShow>() {

            @Override
            public void call(GsonTvShow gsonTvShow) {
                String json = gson.toJson(gsonTvShow, GsonTvShow.class);
                localDataRepository.writeJsonShowDetails(tmdbShowId, json);
            }

        };
    }

    private static Observable<String> fetchJsonTvShowFrom(final LocalDataRepository repository, final com.ataulm.wutson.shows.ShowId showId) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(repository.readJsonShowDetails(showId));
                subscriber.onCompleted();
            }

        });
    }

    private static Func2<Configuration, GsonTvShow, com.ataulm.wutson.shows.Show> asShow(final com.ataulm.wutson.shows.ShowId showId) {
        return new Func2<Configuration, GsonTvShow, com.ataulm.wutson.shows.Show>() {

            @Override
            public com.ataulm.wutson.shows.Show call(Configuration configuration, GsonTvShow gsonTvShow) {
                List<Character> characters = getCharacters(configuration, gsonTvShow);

                String name = gsonTvShow.name;
                String overview = gsonTvShow.overview;
                URI posterUri = configuration.completePoster(gsonTvShow.posterPath);
                URI backdropUri = configuration.completeBackdrop(gsonTvShow.backdropPath);
                com.ataulm.wutson.shows.Cast cast = new com.ataulm.wutson.shows.Cast(characters);

                List<com.ataulm.wutson.shows.Show.SeasonSummary> seasonSummaries = getSeasons(configuration, gsonTvShow);
                com.ataulm.wutson.shows.ShowId id = new com.ataulm.wutson.shows.ShowId(gsonTvShow.id);
                return new com.ataulm.wutson.shows.Show(id, name, overview, posterUri, backdropUri, cast, seasonSummaries);
            }

            private List<Character> getCharacters(Configuration configuration, GsonTvShow gsonTvShow) {
                List<Character> characters = new ArrayList<>();
                for (GsonCredits.Cast.Entry entry : gsonTvShow.gsonCredits.cast) {
                    com.ataulm.wutson.shows.Actor actor = new com.ataulm.wutson.shows.Actor(entry.actorName, configuration.completeProfile(entry.profilePath));
                    characters.add(new Character(entry.name, actor));
                }
                return characters;
            }

            private List<com.ataulm.wutson.shows.Show.SeasonSummary> getSeasons(Configuration configuration, GsonTvShow gsonTvShow) {
                List<com.ataulm.wutson.shows.Show.SeasonSummary> seasonSummaries = new ArrayList<>();
                for (GsonTvShow.Season season : gsonTvShow.seasons) {
                    String id = season.id;
                    String showName = gsonTvShow.name;
                    int seasonNumber = season.seasonNumber;
                    int episodeCount = season.episodeCount;
                    URI posterPath = configuration.completePoster(season.posterPath);
                    seasonSummaries.add(new com.ataulm.wutson.shows.Show.SeasonSummary(id, showId, showName, seasonNumber, episodeCount, posterPath));
                }
                return seasonSummaries;
            }

        };
    }

}
