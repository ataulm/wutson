package com.ataulm.wutson.repository;

import com.ataulm.wutson.shows.Actor;
import com.ataulm.wutson.episodes.Episode;
import com.ataulm.wutson.episodes.Episodes;
import com.ataulm.wutson.seasons.Season;
import com.ataulm.wutson.seasons.Seasons;
import com.ataulm.wutson.shows.Show;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.ShowSummaries;
import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.shows.TrackedStatus;
import com.ataulm.wutson.shows.WatchedStatus;
import com.ataulm.wutson.shows.discover.ShowsInGenre;
import com.ataulm.wutson.shows.myshows.Watchlist;
import com.ataulm.wutson.shows.myshows.WatchlistItem;
import com.ataulm.wutson.rx.Function;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.observables.GroupedObservable;

public class WutsonDataRepository implements DataRepository {

    private final TrackedShowsRepository trackedShowsRepo;
    private final ShowsInGenreRepository showsInGenreRepo;
    private final ShowRepository showRepo;
    private final SeasonsRepository seasonsRepo;

    public WutsonDataRepository(TrackedShowsRepository trackedShowsRepo, ShowsInGenreRepository showsInGenreRepo, ShowRepository showRepo,
                                SeasonsRepository seasonsRepo) {
        this.trackedShowsRepo = trackedShowsRepo;
        this.showsInGenreRepo = showsInGenreRepo;
        this.showRepo = showRepo;
        this.seasonsRepo = seasonsRepo;
    }

    @Override
    public Observable<ShowSummaries> getMyShows() {
        return trackedShowsRepo.getMyShows();
    }

    @Override
    public Observable<TrackedStatus> getTrackedStatus(ShowId showId) {
        return getMyShows().map(asTrackedStatus(showId));
    }

    private static Func1<ShowSummaries, TrackedStatus> asTrackedStatus(final ShowId showId) {
        return new Func1<ShowSummaries, TrackedStatus>() {

            @Override
            public TrackedStatus call(ShowSummaries showSummaries) {
                if (showSummaries.contains(showId)) {
                    return TrackedStatus.TRACKED;
                }
                return TrackedStatus.NOT_TRACKED;
            }

        };
    }

    @Override
    public void toggleTrackedStatus(ShowId showId) {
        trackedShowsRepo.toggleTrackedStatus(showId);
    }

    @Override
    public void setTrackedStatus(ShowId showId, TrackedStatus trackedStatus) {
        trackedShowsRepo.setTrackedStatus(showId, trackedStatus);
    }

    @Override
    public Observable<Watchlist> getWatchlist() {
        return getMyShows().first()
                .flatMap(Function.<ShowSummary>emitEachElement())
                .flatMap(new Func1<ShowSummary, Observable<WatchlistItem>>() {
                    @Override
                    public Observable<WatchlistItem> call(ShowSummary showSummary) {
                        return getSeasons(showSummary.getId())
                                .concatMap(Function.<Season>emitEachElement())
                                .concatMap(Function.<Episode>emitEachElement())
                                .filter(onlyUnwatchedEpisodes())
                                .take(5)
                                .toList()
                                .map(asWatchListItem());
                    }
                })
                .toList()
                .map(asWatchlist());
    }

    private static Func1<List<WatchlistItem>, Watchlist> asWatchlist() {
        return new Func1<List<WatchlistItem>, Watchlist>() {
            @Override
            public Watchlist call(List<WatchlistItem> watchlistItems) {
                return new Watchlist(watchlistItems);
            }
        };
    }

    private static Func1<List<Episode>, WatchlistItem> asWatchListItem() {
        return new Func1<List<Episode>, WatchlistItem>() {
            @Override
            public WatchlistItem call(List<Episode> episodes) {
                return new WatchlistItem(episodes.get(0).getShowName(), new Episodes(episodes));
            }
        };
    }

    private static Func1<Episode, Boolean> onlyUnwatchedEpisodes() {
        return new Func1<Episode, Boolean>() {
            @Override
            public Boolean call(Episode episode) {
                // TODO: if the episode has been marked as watched, return false!
                return true;
            }
        };
    }

    @Override
    public Observable<List<ShowsInGenre>> getDiscoverShows() {
        return showsInGenreRepo.getDiscoverShowsList();
    }

    @Override
    public Observable<Show> getShow(ShowId id) {
        return showRepo.getShowDetails(id);
    }

    @Override
    public Observable<Episodes> getWatchedEpisodes(Season season) {
        return Observable.empty();
    }

    @Override
    public Observable<WatchedStatus> getWatchedStatus(Episode episode) {
        return Observable.empty();
    }

    @Override
    public Observable<Integer> getWatchedCount(Season season) {
        return Observable.empty();
    }

    @Override
    public Observable<Season> getSeason(ShowId showId, int seasonNumber) {
        return seasonsRepo.getSeason(showId, seasonNumber);
    }

    @Override
    public Observable<Seasons> getSeasons(ShowId showId) {
        return seasonsRepo.getSeasons(showId);
    }

    @Override
    public Observable<Actor> getActor(String id) {
        return Observable.empty();
    }

    @Override
    public void setWatchedStatus(Episode episode, WatchedStatus watchedStatus) {

    }

    @Override
    public void setWatchedStatus(Season season, WatchedStatus watchedStatus) {

    }

}
