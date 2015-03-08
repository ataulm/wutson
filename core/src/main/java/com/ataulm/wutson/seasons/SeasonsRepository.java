package com.ataulm.wutson.seasons;

import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.show.Show;
import com.ataulm.wutson.show.ShowRepository;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonSeason;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class SeasonsRepository {

    private final TmdbApi api;
    private final ShowRepository showRepository;
    private final ConfigurationRepository configurationRepository;

    public SeasonsRepository(TmdbApi api, ShowRepository showRepository, ConfigurationRepository configurationRepository) {
        this.api = api;
        this.showRepository = showRepository;
        this.configurationRepository = configurationRepository;
    }

    public Observable<Seasons> getSeasons(final String showId) {
        return showRepository.getShow(showId).flatMap(getSeasonSummaries())
                .flatMap(fetchGsonSeasons(showId))
                .flatMap(toSeason())
                .toList()
                .map(asSeasons());
    }

    private Func1<Show, Observable<Show.Season>> getSeasonSummaries() {
        return new Func1<Show, Observable<Show.Season>>() {

            @Override
            public Observable<Show.Season> call(Show show) {
                return Observable.from(show.getSeasons());
            }

        };
    }

    private Func1<Show.Season, Observable<GsonSeason>> fetchGsonSeasons(final String showId) {
        return new Func1<Show.Season, Observable<GsonSeason>>() {

            @Override
            public Observable<GsonSeason> call(Show.Season season) {
                return api.getSeason(showId, season.getSeasonNumber());
            }

        };
    }

    private Func1<GsonSeason, Observable<Season>> toSeason() {
        return new Func1<GsonSeason, Observable<Season>>() {

            @Override
            public Observable<Season> call(GsonSeason gsonSeason) {
                List<Season.Episode> episodes = new ArrayList<>(gsonSeason.episodes.size());
                for (GsonSeason.Episodes.Episode gsonEpisode : gsonSeason.episodes) {
                    episodes.add(new Season.Episode(
                            gsonEpisode.airDate,
                            gsonEpisode.episodeNumber,
                            gsonEpisode.name,
                            gsonEpisode.overview,
                            gsonEpisode.stillPath != null ? URI.create(gsonEpisode.stillPath) : URI.create("") // TODO: this needs Configuration
                    ));
                }

                return Observable.just(new Season(
                        gsonSeason.airDate,
                        gsonSeason.seasonNumber,
                        gsonSeason.overview,
                        gsonSeason.posterPath != null ? URI.create(gsonSeason.posterPath) : URI.create(""), // TODO: this needs Configuration
                        episodes));
            }

        };
    }

    private Func1<List<Season>, Seasons> asSeasons() {
        return new Func1<List<Season>, Seasons>() {

            @Override
            public Seasons call(List<Season> seasons) {
                return new Seasons(seasons);
            }

        };
    }

}
