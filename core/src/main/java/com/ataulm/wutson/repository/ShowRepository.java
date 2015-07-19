package com.ataulm.wutson.repository;

import com.ataulm.wutson.repository.persistence.JsonRepository;
import com.ataulm.wutson.repository.persistence.LocalDataRepository;
import com.ataulm.wutson.shows.Cast;
import com.ataulm.wutson.shows.Character;
import com.ataulm.wutson.shows.Show;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.trakt.GsonShowDetails;
import com.ataulm.wutson.trakt.GsonShowSeason;
import com.ataulm.wutson.trakt.GsonShowSeasonsList;
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

import static com.ataulm.wutson.rx.Function.ignoreEmptyStrings;
import static com.ataulm.wutson.rx.Function.jsonTo;

public class ShowRepository {

    private final TraktApi traktApi;
    private final JsonRepository jsonRepository;
    private final Gson gson;

    public ShowRepository(TraktApi traktApi, JsonRepository jsonRepository, TmdbApi api, LocalDataRepository localDataRepository, ConfigurationRepository configurationRepository, Gson gson) {
        this.traktApi = traktApi;
        this.jsonRepository = jsonRepository;
        this.gson = gson;
    }

    public Observable<Show> getShowDetails(ShowId showId) {
        Observable<GsonShowDetails> gsonShowDetailsObservable = Observable.concat(gsonShowDetailsFromDisk(showId), gsonShowDetailsFromNetwork(showId))
                .first();

        Observable<GsonShowSeasonsList> gsonShowSeasonsObservable = Observable.concat(gsonShowSeasonsFromDisk(showId), gsonShowSeasonsFromNetwork(showId))
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

    private Observable<GsonShowSeasonsList> gsonShowSeasonsFromDisk(ShowId showId) {
        return fetchJsonShowSeasonsFrom(jsonRepository, showId)
                .filter(ignoreEmptyStrings())
                .map(jsonTo(GsonShowSeasonsList.class, gson));
    }

    private Observable<GsonShowSeasonsList> gsonShowSeasonsFromNetwork(ShowId showId) {
        return traktApi.getShowSeasons(showId.toString())
                .doOnNext(saveShowSeasonsAsJsonTo(jsonRepository, showId, gson));
    }

    private static Observable<String> fetchJsonShowSeasonsFrom(final JsonRepository jsonRepository, final ShowId showId) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(jsonRepository.readShowDetails(showId));
                subscriber.onCompleted();
            }

        });
    }

    private static Action1<GsonShowSeasonsList> saveShowSeasonsAsJsonTo(final JsonRepository jsonRepository, final ShowId showId, final Gson gson) {
        return new Action1<GsonShowSeasonsList>() {

            @Override
            public void call(GsonShowSeasonsList gsonShowSeasonsList) {
                String json = gson.toJson(gsonShowSeasonsList, GsonShowSeasonsList.class);
                jsonRepository.writeShowDetails(showId, json);
            }

        };
    }

    private static Func2<GsonShowDetails, GsonShowSeasonsList, Show> asShow() {
        return new Func2<GsonShowDetails, GsonShowSeasonsList, Show>() {
            @Override
            public Show call(GsonShowDetails gsonShowDetails, GsonShowSeasonsList gsonShowSeasonsList) {
                ShowId id = new ShowId(gsonShowDetails.ids.trakt);
                String title = gsonShowDetails.title;

                URI posterUri = URI.create(gsonShowDetails.images.poster.thumb);
                URI backdropUri = URI.create(gsonShowDetails.images.poster.medium);
                List<Show.SeasonSummary> seasonSummaries = new ArrayList<>(gsonShowSeasonsList.size());
                for (GsonShowSeason gsonShowSeason : gsonShowSeasonsList) {
                    Show.SeasonSummary seasonSummary = new Show.SeasonSummary(
                            gsonShowSeason.ids.trakt,
                            id,
                            title,
                            gsonShowSeason.number,
                            gsonShowSeason.episodes.size(),
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

}
