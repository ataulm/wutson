package com.ataulm.wutson.repository;

import com.ataulm.wutson.DataRepository;
import com.ataulm.wutson.model.Actor;
import com.ataulm.wutson.model.Episode;
import com.ataulm.wutson.model.Episodes;
import com.ataulm.wutson.model.GroupedShowSummaries;
import com.ataulm.wutson.model.ShowSummaries;
import com.ataulm.wutson.model.ShowsInGenre;
import com.ataulm.wutson.model.Season;
import com.ataulm.wutson.model.Seasons;
import com.ataulm.wutson.model.Show;
import com.ataulm.wutson.model.TrackedStatus;
import com.ataulm.wutson.model.WatchedStatus;

import java.util.List;

import rx.Observable;

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
    public Observable<TrackedStatus> getTrackedStatus(String showId) {
        return trackedShowsRepo.getTrackedStatusOfShowWith(showId);
    }

    @Override
    public void toggleTrackedStatus(String showId) {
        trackedShowsRepo.toggleTrackedStatus(showId);
    }

    @Override
    public void setTrackedStatus(String showId, TrackedStatus trackedStatus) {
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
    public Observable<Show> getShow(String id) {
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
    public Observable<Season> getSeason(String showId, int seasonNumber) {
        return seasonsRepo.getSeason(showId, seasonNumber);
    }

    @Override
    public Observable<Seasons> getSeasons(String showId) {
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
