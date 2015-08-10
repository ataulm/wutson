package com.ataulm.wutson.repository;

import com.ataulm.wutson.episodes.Episode;
import com.ataulm.wutson.episodes.EpisodeNumber;
import com.ataulm.wutson.repository.persistence.JsonRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.seasons.Season;
import com.ataulm.wutson.seasons.Seasons;
import com.ataulm.wutson.shows.Cast;
import com.ataulm.wutson.shows.Character;
import com.ataulm.wutson.shows.Show;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.SimpleDate;
import com.ataulm.wutson.trakt.GsonShowDetails;
import com.ataulm.wutson.trakt.GsonShowEpisode;
import com.ataulm.wutson.trakt.GsonShowSeason;
import com.ataulm.wutson.trakt.GsonShowSeasonList;
import com.ataulm.wutson.trakt.TraktApi;
import com.google.gson.Gson;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

import static com.ataulm.wutson.rx.Function.ignoreEmptyStrings;
import static com.ataulm.wutson.rx.Function.jsonTo;

public class ShowRepository {

    private final TraktApi traktApi;
    private final JsonRepository jsonRepository;
    private final Gson gson;

    public ShowRepository(TraktApi traktApi, JsonRepository jsonRepository, Gson gson) {
        this.traktApi = traktApi;
        this.jsonRepository = jsonRepository;
        this.gson = gson;
    }

    public Observable<Show> getShowDetails(ShowId showId) {
        Observable<GsonShowDetails> gsonShowDetailsObservable = Observable.concat(gsonShowDetailsFromDisk(showId), gsonShowDetailsFromNetwork(showId))
                .first();

        Observable<GsonShowSeasonList> gsonShowSeasonsObservable = Observable.concat(gsonShowSeasonsFromDisk(showId), gsonShowSeasonsFromNetwork(showId))
                .first();

        return Observable.zip(gsonShowDetailsObservable, gsonShowSeasonsObservable, asShow());
    }

    private Observable<GsonShowDetails> gsonShowDetailsFromDisk(ShowId showId) {
        return fetchJsonShowDetailsFrom(jsonRepository, showId)
                .filter(ignoreEmptyStrings())
                .map(jsonTo(GsonShowDetails.class, gson));
    }

    private Observable<GsonShowDetails> gsonShowDetailsFromNetwork(ShowId showId) {
        return traktApi.getShowDetails(showId.toString())
                .doOnNext(saveShowDetailsAsJsonTo(jsonRepository, showId, gson));
    }

    private static Observable<String> fetchJsonShowDetailsFrom(final JsonRepository jsonRepository, final ShowId showId) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(jsonRepository.readShowDetails(showId));
                subscriber.onCompleted();
            }

        });
    }

    private static Action1<GsonShowDetails> saveShowDetailsAsJsonTo(final JsonRepository jsonRepository, final ShowId showId, final Gson gson) {
        return new Action1<GsonShowDetails>() {

            @Override
            public void call(GsonShowDetails gsonShowDetails) {
                String json = gson.toJson(gsonShowDetails, GsonShowDetails.class);
                jsonRepository.writeShowDetails(showId, json);
            }

        };
    }

    private Observable<GsonShowSeasonList> gsonShowSeasonsFromDisk(ShowId showId) {
        return fetchJsonShowSeasonsFrom(jsonRepository, showId)
                .filter(ignoreEmptyStrings())
                .map(jsonTo(GsonShowSeasonList.class, gson));
    }

    private static Observable<String> fetchJsonShowSeasonsFrom(final JsonRepository jsonRepository, final ShowId showId) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(jsonRepository.readSeasons(showId));
                subscriber.onCompleted();
            }

        });
    }

    private Observable<GsonShowSeasonList> gsonShowSeasonsFromNetwork(ShowId showId) {
        return traktApi.getShowSeasons(showId.toString())
                .flatMap(Function.<GsonShowSeason>emitEachElement())
                .filter(onlySeasonsWithEpisodes())
                .toList()
                .map(asGsonShowSeasonList())
                .doOnNext(saveShowSeasonsAsJsonTo(jsonRepository, showId, gson));
    }

    private static Func1<List<GsonShowSeason>, GsonShowSeasonList> asGsonShowSeasonList() {
        return new Func1<List<GsonShowSeason>, GsonShowSeasonList>() {
            @Override
            public GsonShowSeasonList call(List<GsonShowSeason> gsonShowSeasons) {
                GsonShowSeasonList list = new GsonShowSeasonList();
                list.addAll(gsonShowSeasons);
                return list;
            }
        };
    }

    private static Func1<GsonShowSeason, Boolean> onlySeasonsWithEpisodes() {
        return new Func1<GsonShowSeason, Boolean>() {
            @Override
            public Boolean call(GsonShowSeason gsonShowSeason) {
                return gsonShowSeason.episodes != null && gsonShowSeason.episodes.size() > 0;
            }
        };
    }

    private static Action1<GsonShowSeasonList> saveShowSeasonsAsJsonTo(final JsonRepository jsonRepository, final ShowId showId, final Gson gson) {
        return new Action1<GsonShowSeasonList>() {

            @Override
            public void call(GsonShowSeasonList gsonShowSeasonList) {
                String json = gson.toJson(gsonShowSeasonList, GsonShowSeasonList.class);
                jsonRepository.writeSeasons(showId, json);
            }

        };
    }

    private static Func2<GsonShowDetails, GsonShowSeasonList, Show> asShow() {
        return new Func2<GsonShowDetails, GsonShowSeasonList, Show>() {
            @Override
            public Show call(GsonShowDetails gsonShowDetails, GsonShowSeasonList gsonShowSeasonList) {
                ShowId id = new ShowId(gsonShowDetails.ids.trakt);
                String title = gsonShowDetails.title;

                URI posterUri = URI.create(gsonShowDetails.images.poster.thumb);
                URI backdropUri = URI.create(gsonShowDetails.images.poster.medium);
                List<Show.SeasonSummary> seasonSummaries = new ArrayList<>(gsonShowSeasonList.size());
                for (GsonShowSeason gsonShowSeason : gsonShowSeasonList) {
                    Show.SeasonSummary seasonSummary = getSeasonSummary(id, title, gsonShowSeason);
                    seasonSummaries.add(seasonSummary);
                }

                return new Show(
                        id,
                        title,
                        gsonShowDetails.overview == null ? "" : gsonShowDetails.overview.trim(),
                        posterUri,
                        backdropUri,
                        new Cast(Collections.<Character>emptyList()),
                        seasonSummaries
                );
            }

            private Show.SeasonSummary getSeasonSummary(ShowId id, String title, GsonShowSeason gsonShowSeason) {
                return new Show.SeasonSummary(
                        gsonShowSeason.ids.trakt,
                        id,
                        title,
                        gsonShowSeason.number,
                        gsonShowSeason.episodes == null ? 0 : gsonShowSeason.episodes.size(),
                        gsonShowSeason.images.poster.medium == null ? URI.create("") : URI.create(gsonShowSeason.images.poster.medium));
            }

        };
    }

    public Observable<Seasons> getSeasons(ShowId showId, String showName) {
        return Observable.concat(gsonShowSeasonsFromDisk(showId), gsonShowSeasonsFromNetwork(showId))
                .first()
                .flatMap(Function.<GsonShowSeason>emitEachElement())
                .map(asSeason(showName))
                .toList()
                .map(asSeasons());
    }

    private static Func1<GsonShowSeason, Season> asSeason(final String showName) {
        return new Func1<GsonShowSeason, Season>() {
            @Override
            public Season call(GsonShowSeason gsonShowSeason) {
                return new Season(gsonShowSeason.number, episodesListFrom(gsonShowSeason));
            }

            private List<Episode> episodesListFrom(GsonShowSeason gsonShowSeason) {
                if (gsonShowSeason.episodes == null) {
                    return Collections.emptyList();
                }
                List<Episode> episodes = new ArrayList<>(gsonShowSeason.episodes.size());
                for (GsonShowEpisode gsonShowEpisode : gsonShowSeason.episodes) {
                    Episode episode = episodeFrom(gsonShowEpisode);
                    episodes.add(episode);
                }
                return episodes;
            }

            private Episode episodeFrom(GsonShowEpisode gsonShowEpisode) {
                SimpleDate airDate = SimpleDate.from(gsonShowEpisode.firstAiredDate);
                return new Episode(
                        airDate,
                        new EpisodeNumber(gsonShowEpisode.season, gsonShowEpisode.number),
                        gsonShowEpisode.title,
                        gsonShowEpisode.overview,
                        gsonShowEpisode.images.screenshot.medium == null ? URI.create("") : URI.create(gsonShowEpisode.images.screenshot.medium),
                        showName
                );
            }
        };
    }

    private static Func1<List<Season>, Seasons> asSeasons() {
        return new Func1<List<Season>, Seasons>() {
            @Override
            public Seasons call(List<Season> seasons) {
                return new Seasons(seasons);
            }
        };
    }

}
