package com.ataulm.wutson.shows.discover;

import com.ataulm.wutson.Log;
import com.ataulm.wutson.repository.event.Event;
import com.ataulm.wutson.repository.persistence.JsonRepository;
import com.ataulm.wutson.repository.persistence.Timestamp;
import com.ataulm.wutson.rx.EventFunctions;
import com.ataulm.wutson.rx.Functions;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.ShowSummaries;
import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.trakt.GsonPopularShowList;
import com.ataulm.wutson.trakt.GsonTrendingShowList;
import com.ataulm.wutson.trakt.TraktApi;
import com.ataulm.wutson.trakt.gson.GsonShowSummary;
import com.ataulm.wutson.trakt.gson.GsonTrendingShow;
import com.google.gson.Gson;

import java.net.URI;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import static com.ataulm.wutson.rx.Functions.ignoreEmptyStrings;
import static com.ataulm.wutson.rx.Functions.jsonTo;

public class DiscoverShowsRepository {

    private static final int HOURS_TIL_STALE_DATA = 36;
    private final TraktApi traktApi;
    private final JsonRepository jsonRepository;
    private final Gson gson;
    private final Log log;
    private final BehaviorSubject<Event<DiscoverShows>> subject;

    public DiscoverShowsRepository(TraktApi traktApi, JsonRepository jsonRepository, Gson gson, Log log) {
        this.traktApi = traktApi;
        this.jsonRepository = jsonRepository;
        this.gson = gson;
        this.log = log;
        this.subject = BehaviorSubject.create();
    }

    public Observable<Event<DiscoverShows>> getDiscoverShowsEvents() {
        refreshDiscoverShows(false);
        return subject;
    }

    public void refreshDiscoverShows() {
        refreshDiscoverShows(true);
    }

    private void refreshDiscoverShows(boolean forceLoadFromNetwork) {
        Observable<DiscoverShows> discoverShows = forceLoadFromNetwork ? getDiscoverShowsFromNetwork() : getDiscoverShows();
        discoverShows
                .doOnSubscribe(EventFunctions.sendLoadingEventTo(subject))
                .map(EventFunctions.<DiscoverShows>asIdleEventWithData())
                .onErrorReturn(EventFunctions.sendErrorEventTo(subject))
                .switchIfEmpty(EventFunctions.sendIdleEventTo(subject))
                .lift(Functions.<Event<DiscoverShows>>swallowOnCompleteEvents())
                .subscribe(subject);
    }

    private Observable<DiscoverShows> getDiscoverShows() {
        Observable<DiscoverShows> disk = getDiscoverShowsFromDisk();
        Observable<DiscoverShows> network = getDiscoverShowsFromNetwork();
        return Observable.concat(disk, network).first()
                .subscribeOn(Schedulers.io());
    }

    private Observable<DiscoverShows> getDiscoverShowsFromDisk() {
        return getDiscoverShows(gsonPopularShowsListFromDisk(), gsonTrendingShowsListFromDisk());
    }

    public Observable<DiscoverShows> getDiscoverShowsFromNetwork() {
        return getDiscoverShows(gsonPopularShowsListFromNetwork(), gsonTrendingShowsListFromNetwork())
                .subscribeOn(Schedulers.io());
    }

    private Observable<DiscoverShows> getDiscoverShows(Observable<GsonPopularShowList> popularShowsObservable, Observable<GsonTrendingShowList> trendingShowsObservable) {
        return Observable.zip(
                showSummariesFromPopularShowsList(popularShowsObservable),
                showSummariesFromTrendingShowsList(trendingShowsObservable),
                asDiscoverShows()
        );
    }

    private Observable<GsonPopularShowList> gsonPopularShowsListFromDisk() {
        return fetchJsonPopularShowsListFrom(jsonRepository)
                .filter(ignoreEmptyStrings())
                .map(jsonTo(GsonPopularShowList.class, gson));
    }

    private Observable<GsonPopularShowList> gsonPopularShowsListFromNetwork() {
        return traktApi.getPopularShows()
                .doOnNext(savePopularShowsListsAsJsonTo(jsonRepository, gson));
    }

    private static Observable<String> fetchJsonPopularShowsListFrom(final JsonRepository jsonRepository) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                Timestamp timestamp = new Timestamp(jsonRepository.readPopularShowsCreatedDate());
                long hours = timestamp.differenceInHours(Timestamp.now());

                if (hours < HOURS_TIL_STALE_DATA) {
                    subscriber.onNext(jsonRepository.readPopularShowsList());
                }

                subscriber.onCompleted();
            }

        });
    }

    private static Action1<GsonPopularShowList> savePopularShowsListsAsJsonTo(final JsonRepository jsonRepository, final Gson gson) {
        return new Action1<GsonPopularShowList>() {

            @Override
            public void call(GsonPopularShowList gsonPopularShowList) {
                String json = gson.toJson(gsonPopularShowList, GsonPopularShowList.class);
                jsonRepository.writePopularShowsList(json);
            }

        };
    }

    private Observable<GsonTrendingShowList> gsonTrendingShowsListFromDisk() {
        return fetchJsonTrendingShowsListFrom(jsonRepository)
                .filter(ignoreEmptyStrings())
                .map(jsonTo(GsonTrendingShowList.class, gson));
    }

    private Observable<GsonTrendingShowList> gsonTrendingShowsListFromNetwork() {
        return traktApi.getTrendingShows()
                .doOnNext(saveTrendingShowsListsAsJsonTo(jsonRepository, gson));
    }

    private static Observable<String> fetchJsonTrendingShowsListFrom(final JsonRepository jsonRepository) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                Timestamp timestamp = new Timestamp(jsonRepository.readTrendingShowsCreatedDate());
                long hours = timestamp.differenceInHours(Timestamp.now());

                if (hours < HOURS_TIL_STALE_DATA) {
                    String t = jsonRepository.readTrendingShowsList();
                    subscriber.onNext(t);
                }

                subscriber.onCompleted();
            }

        });
    }

    private static Action1<GsonTrendingShowList> saveTrendingShowsListsAsJsonTo(final JsonRepository jsonRepository, final Gson gson) {
        return new Action1<GsonTrendingShowList>() {

            @Override
            public void call(GsonTrendingShowList gsonTrendingShowList) {
                String json = gson.toJson(gsonTrendingShowList, GsonTrendingShowList.class);
                jsonRepository.writeTrendingShowsList(json);
            }

        };
    }

    private static Observable<ShowSummaries> showSummariesFromPopularShowsList(Observable<GsonPopularShowList> shows) {
        return shows
                .flatMap(Functions.<GsonShowSummary>emitEachElement())
                .map(asShowSummary())
                .toList()
                .filter(onlyNonEmpty())
                .map(asShowSummaries());
    }

    private Observable<ShowSummaries> showSummariesFromTrendingShowsList(Observable<GsonTrendingShowList> shows) {
        return shows
                .flatMap(Functions.<GsonTrendingShow>emitEachElement())
                .map(extractGsonShowSummary())
                .map(asShowSummary())
                .toList()
                .filter(onlyNonEmpty())
                .map(asShowSummaries());
    }

    private static Func1<List, Boolean> onlyNonEmpty() {
        return new Func1<List, Boolean>() {
            @Override
            public Boolean call(List list) {
                return !list.isEmpty();
            }
        };
    }

    private static Func1<GsonTrendingShow, GsonShowSummary> extractGsonShowSummary() {
        return new Func1<GsonTrendingShow, GsonShowSummary>() {
            @Override
            public GsonShowSummary call(GsonTrendingShow gsonTrendingShow) {
                return gsonTrendingShow.show;
            }
        };
    }

    private static Func1<GsonShowSummary, ShowSummary> asShowSummary() {
        return new Func1<GsonShowSummary, ShowSummary>() {
            @Override
            public ShowSummary call(GsonShowSummary gsonShowSummary) {
                ShowId id = new ShowId(gsonShowSummary.ids.trakt);
                URI posterUri = gsonShowSummary.images.poster.thumb == null ? URI.create("") : URI.create(gsonShowSummary.images.poster.thumb);
                URI backdropUri = gsonShowSummary.images.poster.medium == null ? URI.create("") : URI.create(gsonShowSummary.images.poster.medium);
                return new ShowSummary(id, gsonShowSummary.title, posterUri, backdropUri);

            }
        };
    }

    private static Func1<List<ShowSummary>, ShowSummaries> asShowSummaries() {
        return new Func1<List<ShowSummary>, ShowSummaries>() {
            @Override
            public ShowSummaries call(List<ShowSummary> showSummaries) {
                return new ShowSummaries(showSummaries);
            }
        };
    }

    private static Func2<ShowSummaries, ShowSummaries, DiscoverShows> asDiscoverShows() {
        return new Func2<ShowSummaries, ShowSummaries, DiscoverShows>() {
            @Override
            public DiscoverShows call(ShowSummaries popularShows, ShowSummaries trendingShows) {
                return new DiscoverShows(popularShows, trendingShows);
            }
        };
    }

}
