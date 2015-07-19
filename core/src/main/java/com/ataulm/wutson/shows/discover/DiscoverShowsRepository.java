package com.ataulm.wutson.shows.discover;

import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.ShowSummaries;
import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.trakt.TraktApi;
import com.ataulm.wutson.trakt.gson.GsonTrendingShow;
import com.ataulm.wutson.trakt.gson.GsonShow;

import java.net.URI;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class DiscoverShowsRepository {

    private final TraktApi traktApi;

    private final BehaviorSubject<DiscoverShows> subject;

    public DiscoverShowsRepository(TraktApi traktApi) {
        this.traktApi = traktApi;
        this.subject = BehaviorSubject.create();
    }

    public Observable<DiscoverShows> getDiscoverShows() {
        if (!subject.hasValue()) {
            refreshListOfShowsInGenre();
        }
        return subject;
    }

    private void refreshListOfShowsInGenre() {
        Observable<ShowSummaries> popularShowsObservable = showSummariesFromPopularShowsList(traktApi.getPopularShows());
        Observable<ShowSummaries> trendingShowsObservable = showSummariesFromTrendingShowsList(traktApi.getTrendingShows());

        Observable.zip(popularShowsObservable, trendingShowsObservable, asDiscoverShows())
                .subscribeOn(Schedulers.io())
                .subscribe(subject);
    }

    private static Observable<ShowSummaries> showSummariesFromPopularShowsList(Observable<List<GsonShow>> shows) {
        return shows
                .flatMap(Function.<GsonShow>emitEachElement())
                .map(asShowSummary())
                .toList()
                .map(asShowSummaries());
    }

    private static Observable<ShowSummaries> showSummariesFromTrendingShowsList(Observable<List<GsonTrendingShow>> shows) {
        return shows
                .flatMap(Function.<GsonTrendingShow>emitEachElement())
                .map(new Func1<GsonTrendingShow, GsonShow>() {
                    @Override
                    public GsonShow call(GsonTrendingShow gsonTrendingShow) {
                        return gsonTrendingShow.show;
                    }
                })
                .map(asShowSummary())
                .toList()
                .map(asShowSummaries());
    }

    private static Func1<GsonTrendingShow, GsonShow> extractGsonShowSummary() {
        return new Func1<GsonTrendingShow, GsonShow>() {
            @Override
            public GsonShow call(GsonTrendingShow gsonTrendingShow) {
                return gsonTrendingShow.show;
            }
        };
    }

    private static Func1<GsonShow, ShowSummary> asShowSummary() {
        return new Func1<GsonShow, ShowSummary>() {
            @Override
            public ShowSummary call(GsonShow gsonShow) {
                ShowId id = new ShowId(gsonShow.ids.trakt);
                URI posterUri = URI.create(gsonShow.images.poster.thumb);
                URI backdropUri = URI.create(gsonShow.images.poster.medium);
                return new ShowSummary(id, gsonShow.title, posterUri, backdropUri);
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
