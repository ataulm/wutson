package com.ataulm.wutson.core.seasons;

import com.ataulm.wutson.core.episodes.Episode;
import com.ataulm.wutson.core.model.TmdbConfiguration;
import com.ataulm.wutson.core.seasons.Season;
import com.ataulm.wutson.core.seasons.Seasons;
import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.core.showdetails.Show;
import com.ataulm.wutson.core.showdetails.ShowRepository;
import com.ataulm.wutson.core.tmdb.TmdbApi;
import com.ataulm.wutson.core.tmdb.gson.GsonSeason;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;

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

        Observable<Season> seasonObservable = showObservable
                .flatMap(forEachSeasonInShow())
                .flatMap(fetchCompleteSeasonFrom(api, configurationRepository));

        return Observable.combineLatest(showObservable, seasonObservable.toSortedList(), asSeasons());
    }

    private static Func1<Show.SeasonSummary, Observable<Season>> fetchCompleteSeasonFrom(final TmdbApi api, final ConfigurationRepository configurationRepository) {
        return new Func1<Show.SeasonSummary, Observable<Season>>() {
            @Override
            public Observable<Season> call(Show.SeasonSummary seasonSummary) {
                return Observable.zip(
                        configurationRepository.getConfiguration().first(),
                        api.getSeason(seasonSummary.getShowId(), seasonSummary.getSeasonNumber()),
                        Observable.just(seasonSummary),
                        asSeason());
            }
        };
    }

    private static Func3<TmdbConfiguration, GsonSeason, Show.SeasonSummary, Season> asSeason() {
        return new Func3<TmdbConfiguration, GsonSeason, Show.SeasonSummary, Season>() {
            @Override
            public Season call(TmdbConfiguration configuration, GsonSeason gsonSeason, Show.SeasonSummary seasonSummary) {
                List<Episode> episodes = new ArrayList<>(gsonSeason.episodes.size());
                for (GsonSeason.Episodes.Episode gsonEpisode : gsonSeason.episodes) {
                    episodes.add(new Episode(
                            gsonEpisode.airDate,
                            gsonSeason.seasonNumber,
                            gsonEpisode.episodeNumber,
                            gsonEpisode.name,
                            gsonEpisode.overview,
                            configuration.completeStill(gsonEpisode.stillPath),
                            seasonSummary.getShowName()));
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

    private Func1<Show, Observable<Show.SeasonSummary>> forEachSeasonInShow() {
        return new Func1<Show, Observable<Show.SeasonSummary>>() {

            @Override
            public Observable<Show.SeasonSummary> call(Show show) {
                Iterable<Show.SeasonSummary> seasons = show.getSeasonSummaries();
                return Observable.from(seasons);
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
