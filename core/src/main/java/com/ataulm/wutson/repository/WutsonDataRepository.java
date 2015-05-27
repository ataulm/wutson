package com.ataulm.wutson.repository;

import com.ataulm.wutson.DataRepository;
import com.ataulm.wutson.model.Actor;
import com.ataulm.wutson.model.Episode;
import com.ataulm.wutson.model.Episodes;
import com.ataulm.wutson.model.GroupedShowSummaries;
import com.ataulm.wutson.model.Season;
import com.ataulm.wutson.model.Seasons;
import com.ataulm.wutson.model.Show;
import com.ataulm.wutson.model.ShowId;
import com.ataulm.wutson.model.ShowSummaries;
import com.ataulm.wutson.model.ShowsInGenre;
import com.ataulm.wutson.model.TrackedStatus;
import com.ataulm.wutson.model.WatchedStatus;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

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

    public Observable<List<GroupedShowSummaries>> getUpcomingShows() {
        return Observable.empty();
    }

    @Override
    public Observable<List<GroupedShowSummaries>> getRecentShows() {
        return Observable.empty();
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
