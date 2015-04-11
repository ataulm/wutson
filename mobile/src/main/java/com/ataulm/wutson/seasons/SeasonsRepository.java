package com.ataulm.wutson.seasons;

import com.ataulm.wutson.episodes.Episode;
import com.ataulm.wutson.model.TmdbConfiguration;
import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.showdetails.Show;
import com.ataulm.wutson.showdetails.ShowRepository;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonSeason;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class SeasonsRepository {

    private final TmdbApi api;
    private final ConfigurationRepository configurationRepository;
    private final ShowRepository showRepository;

    public SeasonsRepository(TmdbApi api, ConfigurationRepository configurationRepository, ShowRepository showRepository) {
        this.api = api;
        this.configurationRepository = configurationRepository;
        this.showRepository = showRepository;
    }

    public Observable<Seasons> getSeasons(final String showId) {
        Observable<Show> showObservable = showRepository.getShowDetails(showId);
        Observable<TmdbConfiguration> configurationObservable = configurationRepository.getConfiguration().first();
        Observable<GsonSeason> gsonSeasonObservable = showObservable
                .flatMap(forEachSeasonInShow())
                .flatMap(fetchGsonSeason());

        Observable<List<Season>> seasonObservable = Observable.combineLatest(gsonSeasonObservable, configurationObservable, asSeason()).toSortedList();

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

    private static Func2<GsonSeason, TmdbConfiguration, Season> asSeason() {
        return new Func2<GsonSeason, TmdbConfiguration, Season>() {

            @Override
            public Season call(GsonSeason gsonSeason, TmdbConfiguration configuration) {
                List<Episode> episodes = new ArrayList<>(gsonSeason.episodes.size());
                for (GsonSeason.Episodes.Episode gsonEpisode : gsonSeason.episodes) {
                    episodes.add(new Episode(
                            gsonEpisode.airDate,
                            gsonSeason.seasonNumber,
                            gsonEpisode.episodeNumber,
                            gsonEpisode.name,
                            gsonEpisode.overview,
                            configuration.completeStill(gsonEpisode.stillPath)));
                }

                return new Season(
                        gsonSeason.airDate,
                        gsonSeason.seasonNumber,
                        gsonSeason.overview,
                        configuration.completePoster(gsonSeason.posterPath),
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

    public Observable<Season> getSeason(String showId, final int seasonNumber) {
        return getSeasons(showId)
                .flatMap(Function.<Season>emitEachElement())
                .filter(seasonsNumber(seasonNumber))
                .first();
    }

    private static Func1<Season, Boolean> seasonsNumber(final int seasonId) {
        return new Func1<Season, Boolean>() {

            @Override
            public Boolean call(Season season) {
                return season.getSeasonNumber() == seasonId;
            }

        };
    }

}
