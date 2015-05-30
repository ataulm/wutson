package com.ataulm.wutson.repository;

import com.ataulm.wutson.DataRepository;
import com.ataulm.wutson.model.Actor;
import com.ataulm.wutson.model.Episode;
import com.ataulm.wutson.model.Episodes;
import com.ataulm.wutson.model.EpisodesByDate;
import com.ataulm.wutson.model.Season;
import com.ataulm.wutson.model.Seasons;
import com.ataulm.wutson.model.Show;
import com.ataulm.wutson.model.ShowId;
import com.ataulm.wutson.model.ShowSummaries;
import com.ataulm.wutson.model.ShowsInGenre;
import com.ataulm.wutson.model.SimpleDate;
import com.ataulm.wutson.model.TrackedStatus;
import com.ataulm.wutson.model.WatchedStatus;
import com.ataulm.wutson.rx.Function;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

import static com.ataulm.wutson.model.SimpleDate.today;

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
    public Observable<EpisodesByDate> getUpcomingEpisodes() {
        return trackedShowsRepo.getTrackedShowIds()
                .flatMap(getAllEpisodes())
                .filter(onlyEpisodesAiringAfter(today()))
                .toList()
                .map(new Func1<List<Episode>, List<Episode>>() {
                    @Override
                    public List<Episode> call(List<Episode> episodes) {
                        Collections.sort(episodes, new EpisodeDateComparator());
                        return episodes;
                    }
                })
                .map(splitIntoEpisodesByDay());
    }

    private Func1<List<ShowId>, Observable<Episode>> getAllEpisodes() {
        return new Func1<List<ShowId>, Observable<Episode>>() {
            @Override
            public Observable<Episode> call(List<ShowId> showIds) {
                return Observable.from(showIds)
                        .flatMap(getSeasons())
                        .flatMap(Function.<Season>emitEachElement())
                        .flatMap(Function.<Episode>emitEachElement());
            }
        };
    }

    private static Func1<List<Episode>, EpisodesByDate> splitIntoEpisodesByDay() {
        return new Func1<List<Episode>, EpisodesByDate>() {

            @Override
            public EpisodesByDate call(List<Episode> episodes) {
                EpisodesByDate.Builder builder = new EpisodesByDate.Builder();
                for (Episode episode : episodes) {
                    builder.add(episode);
                }
                return builder.build();
            }

        };
    }

    private static Func1<Episode, Boolean> onlyEpisodesAiringAfter(final SimpleDate today) {
        return new Func1<Episode, Boolean>() {
            @Override
            public Boolean call(Episode episode) {
                return episode.getAirDate().isAfter(today);
            }
        };
    }

    private Func1<ShowId, Observable<Seasons>> getSeasons() {
        return new Func1<ShowId, Observable<Seasons>>() {
            @Override
            public Observable<Seasons> call(ShowId showId) {
                return getSeasons(showId);
            }
        };
    }

    @Override
    public Observable<EpisodesByDate> getRecentEpisodes() {
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
