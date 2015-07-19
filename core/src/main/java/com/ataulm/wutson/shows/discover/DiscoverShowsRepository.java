package com.ataulm.wutson.shows.discover;

import com.ataulm.wutson.repository.persistence.JsonRepository;
import com.ataulm.wutson.rx.Function;
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

import static com.ataulm.wutson.rx.Function.ignoreEmptyStrings;
import static com.ataulm.wutson.rx.Function.jsonTo;

public class DiscoverShowsRepository {

    private final TraktApi traktApi;
    private final JsonRepository jsonRepository;
    private final Gson gson;

    public DiscoverShowsRepository(TraktApi traktApi, JsonRepository jsonRepository, Gson gson) {
        this.traktApi = traktApi;
        this.jsonRepository = jsonRepository;
        this.gson = gson;
    }

    public Observable<DiscoverShows> getDiscoverShows() {
        Observable<GsonPopularShowList> gsonPopularShowsListObservable = Observable.concat(gsonPopularShowsListFromDisk(), gsonPopularShowsListFromNetwork())
                .first();

        Observable<GsonTrendingShowList> gsonTrendingShowsListObservable = Observable.concat(gsonTrendingShowsListFromDisk(), gsonTrendingShowsListFromNetwork())
                .first();

        Observable<ShowSummaries> popularShowsObservable = showSummariesFromPopularShowsList(gsonPopularShowsListObservable);
        Observable<ShowSummaries> trendingShowsObservable = showSummariesFromTrendingShowsList(gsonTrendingShowsListObservable);

        return Observable.zip(popularShowsObservable, trendingShowsObservable, asDiscoverShows());
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
                subscriber.onNext(jsonRepository.readPopularShowsList());
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
                subscriber.onNext(jsonRepository.readTrendingShowsList());
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
                .flatMap(Function.<GsonShowSummary>emitEachElement())
                .map(asShowSummary())
                .toList()
                .map(asShowSummaries());
    }

    private static Observable<ShowSummaries> showSummariesFromTrendingShowsList(Observable<GsonTrendingShowList> shows) {
        return shows
                .flatMap(Function.<GsonTrendingShow>emitEachElement())
                .map(extractGsonShowSummary())
                .map(asShowSummary())
                .toList()
                .map(asShowSummaries());
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
                URI posterUri = URI.create(gsonShowSummary.images.poster.thumb);
                URI backdropUri = URI.create(gsonShowSummary.images.poster.medium);
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
