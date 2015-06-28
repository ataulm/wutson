package com.ataulm.wutson.repository;

import com.ataulm.wutson.DataRepository;
import com.ataulm.wutson.model.Actor;
import com.ataulm.wutson.episodes.Episode;
import com.ataulm.wutson.episodes.Episodes;
import com.ataulm.wutson.seasons.Season;
import com.ataulm.wutson.seasons.Seasons;
import com.ataulm.wutson.model.Show;
import com.ataulm.wutson.model.ShowId;
import com.ataulm.wutson.model.ShowSummaries;
import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.model.ShowsInGenre;
import com.ataulm.wutson.model.TrackedStatus;
import com.ataulm.wutson.model.WatchedStatus;
import com.ataulm.wutson.myshows.Watchlist;
import com.ataulm.wutson.myshows.WatchlistItem;
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
        return getMyShows().take(1)
                .flatMap(Function.<ShowSummary>emitEachElement())
                .flatMap(getListOfSeasonsForThatShow())
                .flatMap(Function.<Season>emitEachElement())
                .flatMap(Function.<Episode>emitEachElement())
                .filter(onlyUnwatchedEpisodes())
                .groupBy(showName())
                .flatMap(asEpisodeListCappedAt(5))
                .toList()
                .map(asWatchlist());
    }

    private Func1<ShowSummary, Observable<Seasons>> getListOfSeasonsForThatShow() {
        return new Func1<ShowSummary, Observable<Seasons>>() {
            @Override
            public Observable<Seasons> call(ShowSummary showSummary) {
                return getSeasons(showSummary.getId());
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

    private static Func1<Episode, String> showName() {
        return new Func1<Episode, String>() {
            @Override
            public String call(Episode episode) {
                return episode.getShowName();
            }
        };
    }

    private static Func1<GroupedObservable<String, Episode>, Observable<List<Episode>>> asEpisodeListCappedAt(final int maxEpisodes) {
        return new Func1<GroupedObservable<String, Episode>, Observable<List<Episode>>>() {
            @Override
            public Observable<List<Episode>> call(GroupedObservable<String, Episode> episodesFromSingleShow) {
                return episodesFromSingleShow.take(maxEpisodes).toList();
            }
        };
    }

    private static Func1<List<List<Episode>>, Watchlist> asWatchlist() {
        return new Func1<List<List<Episode>>, Watchlist>() {

            @Override
            public Watchlist call(List<List<Episode>> lists) {
                List<WatchlistItem> watchlistItems = new ArrayList<>();
                for (List<Episode> list : lists) {
                    String showName = list.get(0).getShowName();
                    watchlistItems.add(WatchlistItem.from(showName));
                    for (Episode episode : list) {
                        watchlistItems.add(WatchlistItem.from(episode));
                    }
                }
                return new Watchlist(watchlistItems);
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
