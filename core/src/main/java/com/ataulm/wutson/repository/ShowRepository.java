package com.ataulm.wutson.repository;

import com.ataulm.wutson.episodes.Episode;
import com.ataulm.wutson.episodes.SeasonEpisodeNumber;
import com.ataulm.wutson.repository.event.Event;
import com.ataulm.wutson.repository.persistence.JsonRepository;
import com.ataulm.wutson.rx.EventFunctions;
import com.ataulm.wutson.rx.Functions;
import com.ataulm.wutson.seasons.Season;
import com.ataulm.wutson.seasons.Seasons;
import com.ataulm.wutson.shows.Actor;
import com.ataulm.wutson.shows.Cast;
import com.ataulm.wutson.shows.Character;
import com.ataulm.wutson.shows.Show;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.SimpleDate;
import com.ataulm.wutson.trakt.GsonShowDetails;
import com.ataulm.wutson.trakt.GsonShowEpisode;
import com.ataulm.wutson.trakt.GsonShowPeople;
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
import rx.functions.Func3;

import static com.ataulm.wutson.rx.Functions.onlyNonEmptyStrings;
import static com.ataulm.wutson.rx.Functions.jsonTo;

public class ShowRepository {

    private final TraktApi traktApi;
    private final JsonRepository jsonRepository;
    private final Gson gson;

    public ShowRepository(TraktApi traktApi, JsonRepository jsonRepository, Gson gson) {
        this.traktApi = traktApi;
        this.jsonRepository = jsonRepository;
        this.gson = gson;
    }

    public Observable<Event<Show>> getShowDetailsEvents(ShowId showId) {
        return getShowDetails(showId)
                .compose(EventFunctions.<Show>asEvents());
    }

    public Observable<Show> getShowDetails(ShowId showId) {
        Observable<GsonShowDetails> gsonShowDetailsObservable = Observable.concat(gsonShowDetailsFromDisk(showId), gsonShowDetailsFromNetwork(showId))
                .first();

        Observable<GsonShowSeasonList> gsonShowSeasonsObservable = Observable.concat(gsonShowSeasonsFromDisk(showId), gsonShowSeasonsFromNetwork(showId))
                .first();

        Observable<GsonShowPeople> gsonShowPeopleObservable = Observable.concat(gsonShowPeopleFromDisk(showId), gsonShowPeopleFromNetwork(showId));

        Observable<Cast> castObservable = gsonShowPeopleObservable.first()
                .filter(onlyNonEmptyCast())
                .map(extractJsonCast())
                .flatMap(Functions.<GsonShowPeople.Character>emitEachElement())
                .filter(onlyValidCharacters())
                .map(asCharacter())
                .toList()
                .map(asCast());
        ;

        return Observable.zip(gsonShowDetailsObservable, gsonShowSeasonsObservable, castObservable, asShow());
    }

    private Observable<? extends GsonShowPeople> gsonShowPeopleFromDisk(ShowId showId) {
        return fetchJsonShowPeopleFrom(jsonRepository, showId)
                .filter(onlyNonEmptyStrings())
                .map(jsonTo(GsonShowPeople.class, gson));
    }

    private Observable<? extends GsonShowPeople> gsonShowPeopleFromNetwork(ShowId showId) {
        return traktApi.getShowPeople(showId.toString())
                .doOnNext(saveShowPeopleAsJsonTo(jsonRepository, showId, gson));
    }

    private Observable<GsonShowDetails> gsonShowDetailsFromDisk(ShowId showId) {
        return fetchJsonShowDetailsFrom(jsonRepository, showId)
                .filter(onlyNonEmptyStrings())
                .map(jsonTo(GsonShowDetails.class, gson));
    }

    private Observable<GsonShowDetails> gsonShowDetailsFromNetwork(ShowId showId) {
        return traktApi.getShowDetails(showId.toString())
                .doOnNext(saveShowDetailsAsJsonTo(jsonRepository, showId, gson));
    }

    private static Observable<String> fetchJsonShowPeopleFrom(final JsonRepository jsonRepository, final ShowId showId) {
        return Observable.create(
                new Observable.OnSubscribe<String>() {

                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(jsonRepository.readShowPeople(showId));
                        subscriber.onCompleted();
                    }

                }
        );
    }

    private static Observable<String> fetchJsonShowDetailsFrom(final JsonRepository jsonRepository, final ShowId showId) {
        return Observable.create(
                new Observable.OnSubscribe<String>() {

                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(jsonRepository.readShowDetails(showId));
                        subscriber.onCompleted();
                    }

                }
        );
    }

    private static Action1<GsonShowPeople> saveShowPeopleAsJsonTo(final JsonRepository jsonRepository, final ShowId showId, final Gson gson) {
        return new Action1<GsonShowPeople>() {

            @Override
            public void call(GsonShowPeople gsonShowPeople) {
                String json = gson.toJson(gsonShowPeople, GsonShowPeople.class);
                jsonRepository.writeShowPeople(showId, json);
            }

        };
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
                .filter(onlyNonEmptyStrings())
                .map(jsonTo(GsonShowSeasonList.class, gson));
    }

    private static Observable<String> fetchJsonShowSeasonsFrom(final JsonRepository jsonRepository, final ShowId showId) {
        return Observable.create(
                new Observable.OnSubscribe<String>() {

                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(jsonRepository.readSeasons(showId));
                        subscriber.onCompleted();
                    }

                }
        );
    }

    private Observable<GsonShowSeasonList> gsonShowSeasonsFromNetwork(ShowId showId) {
        return traktApi.getShowSeasons(showId.toString())
                .flatMap(Functions.<GsonShowSeason>emitEachElement())
                .filter(onlySeasonsWithEpisodes())
                .map(removeEmptyEpisodes())
                .filter(onlySeasonsWithEpisodes())
                .toList()
                .map(asGsonShowSeasonList())
                .doOnNext(saveShowSeasonsAsJsonTo(jsonRepository, showId, gson));
    }

    private Func1<GsonShowSeason, GsonShowSeason> removeEmptyEpisodes() {
        return new Func1<GsonShowSeason, GsonShowSeason>() {
            @Override
            public GsonShowSeason call(GsonShowSeason gsonShowSeason) {
                List<GsonShowEpisode> nonEmptyEpisodes = new ArrayList<>();
                for (GsonShowEpisode episode : gsonShowSeason.episodes) {
                    if (episode.firstAiredDate != null && !episode.firstAiredDate.isEmpty()) {
                        nonEmptyEpisodes.add(episode);
                    }
                }
                GsonShowSeason sanitisedGsonShowSeason = new GsonShowSeason();
                sanitisedGsonShowSeason.ids = gsonShowSeason.ids;
                sanitisedGsonShowSeason.images = gsonShowSeason.images;
                sanitisedGsonShowSeason.number = gsonShowSeason.number;
                sanitisedGsonShowSeason.overview = gsonShowSeason.overview;
                sanitisedGsonShowSeason.episodes = nonEmptyEpisodes;
                return sanitisedGsonShowSeason;
            }
        };
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

    private static Func3<GsonShowDetails, GsonShowSeasonList, Cast, Show> asShow() {
        return new Func3<GsonShowDetails, GsonShowSeasonList, Cast, Show>() {
            @Override
            public Show call(GsonShowDetails gsonShowDetails, GsonShowSeasonList gsonShowSeasonList, Cast cast) {
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
                        cast,
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
                        gsonShowSeason.images.poster.medium == null ? URI.create("") : URI.create(gsonShowSeason.images.poster.medium)
                );
            }

        };
    }

    public Observable<Seasons> getSeasons(ShowId showId, String showName) {
        return Observable.concat(gsonShowSeasonsFromDisk(showId), gsonShowSeasonsFromNetwork(showId))
                .first()
                .flatMap(Functions.<GsonShowSeason>emitEachElement())
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
                        new SeasonEpisodeNumber(gsonShowEpisode.season, gsonShowEpisode.number),
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

    public Observable<Season> getSeason(ShowId showId, final int seasonNumber, String showName) {
        return getSeasons(showId, showName)
                .flatMap(findSeasonWithNumber(seasonNumber));
    }

    private static Func1<Seasons, Observable<Season>> findSeasonWithNumber(final int seasonNumber) {
        return new Func1<Seasons, Observable<Season>>() {
            @Override
            public Observable<Season> call(Seasons seasons) {
                for (Season season : seasons) {
                    if (season.getSeasonNumber() == seasonNumber) {
                        return Observable.just(season);
                    }
                }
                return Observable.empty();
            }
        };
    }

    private static Func1<GsonShowPeople.Character, Boolean> onlyValidCharacters() {
        return new Func1<GsonShowPeople.Character, Boolean>() {

            @Override
            public Boolean call(GsonShowPeople.Character character) {
                if (character == null) {
                    return false;
                }

                if (character.character == null || character.character.isEmpty() || character.person == null || character.person.images == null) {
                    return false;
                }

                GsonShowPeople.Images images = character.person.images;
                if (images.headshot == null || images.headshot.thumb == null || images.headshot.thumb.isEmpty()) {
                    return false;
                }

                return true;
            }

        };
    }

    private static Func1<GsonShowPeople, Boolean> onlyNonEmptyCast() {
        return new Func1<GsonShowPeople, Boolean>() {

            @Override
            public Boolean call(GsonShowPeople gsonShowPeople) {
                if (gsonShowPeople == null || gsonShowPeople.cast == null || gsonShowPeople.cast.isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            }

        };
    }

    private static Func1<List<Character>, Cast> asCast() {
        return new Func1<List<Character>, Cast>() {

            @Override
            public Cast call(List<Character> characters) {
                return new Cast(characters);
            }

        };
    }

    private static Func1<GsonShowPeople.Character, Character> asCharacter() {
        return new Func1<GsonShowPeople.Character, Character>() {

            @Override
            public Character call(GsonShowPeople.Character character) {
                GsonShowPeople.Images images = character.person.images;
                Actor actor = new Actor(character.person.name, URI.create(images.headshot.thumb));
                return new Character(character.character, actor);
            }

        };
    }

    private static Func1<GsonShowPeople, GsonShowPeople.Cast> extractJsonCast() {
        return new Func1<GsonShowPeople, GsonShowPeople.Cast>() {

            @Override
            public GsonShowPeople.Cast call(GsonShowPeople gsonShowPeople) {
                return gsonShowPeople.cast;
            }

        };
    }

}
