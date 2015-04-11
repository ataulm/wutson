package com.ataulm.wutson.repository;

import com.ataulm.wutson.discover.ShowsInGenre;
import com.ataulm.wutson.discover.ShowsInGenreRepository;
import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.seasons.Season;
import com.ataulm.wutson.seasons.Seasons;
import com.ataulm.wutson.seasons.SeasonsRepository;
import com.ataulm.wutson.showdetails.Show;
import com.ataulm.wutson.showdetails.ShowRepository;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class DataRepository {

    private final TrackedShowsRepository trackedShowsRepo;
    private final ShowsInGenreRepository showsInGenreRepo;
    private final ShowRepository showRepo;
    private final SeasonsRepository seasonsRepo;

    public DataRepository(TrackedShowsRepository trackedShowsRepo, ShowsInGenreRepository showsInGenreRepo, ShowRepository showRepo, SeasonsRepository seasonsRepo) {
        this.trackedShowsRepo = trackedShowsRepo;
        this.showsInGenreRepo = showsInGenreRepo;
        this.showRepo = showRepo;
        this.seasonsRepo = seasonsRepo;
    }

    public Observable<List<ShowSummary>> getMyShows() {
        return trackedShowsRepo.getMyShows();
    }

    public Observable<Show> getShowDetails(String showId) {
        return showRepo.getShowDetails(showId);
    }

    public Observable<Boolean> getTrackedStatusOfShowWith(String showId) {
        return trackedShowsRepo.getTrackedStatusOfShowWith(showId);
    }

    public Observable<Boolean> toggleTrackedStatusOfShowWith(String showId) {
        return trackedShowsRepo.toggleTrackedStatusOfShowWith(showId);
    }

    public Observable<List<ShowsInGenre>> getDiscoverShowsList() {
        return showsInGenreRepo.getDiscoverShowsList();
    }

    public Observable<Seasons> getSeasons(String showId) {
        return seasonsRepo.getSeasons(showId);
    }

    public Observable<Season> getSeason(String showId, int seasonNumber) {
        return seasonsRepo.getSeason(showId, seasonNumber);
    }

}
