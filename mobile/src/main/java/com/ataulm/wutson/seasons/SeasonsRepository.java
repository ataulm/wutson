package com.ataulm.wutson.seasons;

import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.showdetails.Show;
import com.ataulm.wutson.showdetails.ShowRepository;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonConfiguration;
import com.ataulm.wutson.tmdb.gson.GsonSeason;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

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
        Observable<Show> showObservable = showRepository.getShow(showId);
        Observable<GsonConfiguration> gsonConfigurationObservable = configurationRepository.getConfiguration().first();
        Observable<GsonSeason> gsonSeasonObservable = showObservable
                .flatMap(forEachSeasonInShow())
                .flatMap(fetchGsonSeason());

        Observable<List<Season>> seasonObservable = Observable.combineLatest(gsonSeasonObservable, gsonConfigurationObservable, asSeason()).toSortedList();

        return Observable.combineLatest(showObservable, seasonObservable, asSeasons());
    }

    private Func1<Show, Observable<Show.Season>> forEachSeasonInShow() {
        return new Func1<Show, Observable<Show.Season>>() {

            @Override
            public Observable<Show.Season> call(Show show) {
                Iterable<Show.Season> seasons = show.getSeasons();
                return Observable.from(seasons);
            }

        };
    }

    private Func1<Show.Season, Observable<GsonSeason>> fetchGsonSeason() {
        return new Func1<Show.Season, Observable<GsonSeason>>() {

            @Override
            public Observable<GsonSeason> call(Show.Season season) {
                return api.getSeason(season.getShowId(), season.getSeasonNumber());
            }

        };
    }

    private static Func2<GsonSeason, GsonConfiguration, Season> asSeason() {
        return new Func2<GsonSeason, GsonConfiguration, Season>() {

            @Override
            public Season call(GsonSeason gsonSeason, GsonConfiguration gsonConfiguration) {
                List<Season.Episode> episodes = new ArrayList<>(gsonSeason.episodes.size());
                for (GsonSeason.Episodes.Episode gsonEpisode : gsonSeason.episodes) {
                    episodes.add(new Season.Episode(
                            gsonEpisode.airDate,
                            gsonEpisode.episodeNumber,
                            gsonEpisode.name,
                            gsonEpisode.overview,
                            URI.create(gsonConfiguration.getCompleteStillPath(gsonEpisode.stillPath))
                    ));
                }

                return new Season(
                        gsonSeason.airDate,
                        gsonSeason.seasonNumber,
                        gsonSeason.overview,
                        URI.create(gsonConfiguration.getCompletePosterPath(gsonSeason.posterPath)),
                        episodes);
            }

        };
    }

    private static Func2<Show, List<Season>, Seasons> asSeasons() {
        return new Func2<Show, List<Season>, Seasons>() {

            @Override
            public Seasons call(Show show, List<Season> seasons) {
                return new Seasons(show, seasons);
            }

        };
    }

}
