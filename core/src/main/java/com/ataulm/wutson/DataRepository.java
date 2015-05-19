package com.ataulm.wutson;

import com.ataulm.wutson.model.Actor;
import com.ataulm.wutson.model.Episode;
import com.ataulm.wutson.model.Episodes;
import com.ataulm.wutson.model.GroupedShowSummaries;
import com.ataulm.wutson.model.Season;
import com.ataulm.wutson.model.Seasons;
import com.ataulm.wutson.model.Show;
import com.ataulm.wutson.model.ShowSummaries;
import com.ataulm.wutson.model.ShowsInGenre;
import com.ataulm.wutson.model.TrackedStatus;
import com.ataulm.wutson.model.WatchedStatus;

import java.util.List;

import rx.Observable;

public interface DataRepository {

    Observable<ShowSummaries> getMyShows();

    Observable<List<GroupedShowSummaries>> getUpcomingShows();

    Observable<List<GroupedShowSummaries>> getRecentShows();

    Observable<List<ShowsInGenre>> getDiscoverShows();

    Observable<Show> getShow(String id);

    Observable<TrackedStatus> getTrackedStatus(String showId);

    Observable<Episodes> getWatchedEpisodes(Season season);

    Observable<WatchedStatus> getWatchedStatus(Episode episode);

    Observable<Integer> getWatchedCount(Season season);

    Observable<Season> getSeason(String showId, int seasonNumber);

    Observable<Seasons> getSeasons(String showId);

    Observable<Actor> getActor(String id);

    void toggleTrackedStatus(String showId);

    void setTrackedStatus(String showId, TrackedStatus trackedStatus);

    void setWatchedStatus(Episode episode, WatchedStatus watchedStatus);

    void setWatchedStatus(Season season, WatchedStatus watchedStatus);
}
