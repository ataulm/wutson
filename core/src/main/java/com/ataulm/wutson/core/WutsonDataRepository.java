package com.ataulm.wutson.core;

import com.ataulm.wutson.core.discover.ShowsInGenre;
import com.ataulm.wutson.core.episodes.Episode;
import com.ataulm.wutson.core.seasons.Season;
import com.ataulm.wutson.core.seasons.Seasons;
import com.ataulm.wutson.core.showdetails.Show;

import java.util.List;

import rx.Observable;

public interface WutsonDataRepository {

    Observable<List<ShowsInGenre>> getDiscoverShows();

    Observable<ShowSummaries> getTrackedShows();

    Observable<List<GroupedShowSummaries>> getUpcomingShows();

    Observable<List<GroupedShowSummaries>> getRecentShows();

    Observable<Show> getShow(String id);

    Observable<TrackedStatus> getTrackedStatus(Show show);

    Observable<Episodes> getWatchedEpisodes(Season season);

    Observable<WatchedStatus> getWatchedStatus(Episode episode);

    Observable<Integer> getWatchedCount(Season season);

    Observable<Season> getSeason(String id);

    Observable<Seasons> getSeasons();

    void setTrackedStatus(Show show, TrackedStatus trackedStatus);

    void setWatchedStatus(Episode episode, WatchedStatus watchedStatus);

    void setWatchedStatus(Season season, WatchedStatus watchedStatus);

}
