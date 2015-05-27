package com.ataulm.wutson;

import com.ataulm.wutson.model.Actor;
import com.ataulm.wutson.model.Episode;
import com.ataulm.wutson.model.Episodes;
import com.ataulm.wutson.model.EpisodesByDay;
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

public interface DataRepository {

    Observable<ShowSummaries> getMyShows();

    Observable<List<EpisodesByDay>> getUpcomingEpisodes();

    Observable<List<EpisodesByDay>> getRecentEpisodes();

    Observable<List<ShowsInGenre>> getDiscoverShows();

    Observable<Show> getShow(ShowId id);

    Observable<TrackedStatus> getTrackedStatus(ShowId showId);

    Observable<Episodes> getWatchedEpisodes(Season season);

    Observable<WatchedStatus> getWatchedStatus(Episode episode);

    Observable<Integer> getWatchedCount(Season season);

    Observable<Season> getSeason(ShowId showId, int seasonNumber);

    Observable<Seasons> getSeasons(ShowId showId);

    Observable<Actor> getActor(String id);

    void toggleTrackedStatus(ShowId showId);

    void setTrackedStatus(ShowId showId, TrackedStatus trackedStatus);

    void setWatchedStatus(Episode episode, WatchedStatus watchedStatus);

    void setWatchedStatus(Season season, WatchedStatus watchedStatus);
}
