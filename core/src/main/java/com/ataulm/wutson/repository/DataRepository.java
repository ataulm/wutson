package com.ataulm.wutson.repository;

import com.ataulm.wutson.shows.Actor;
import com.ataulm.wutson.episodes.Episode;
import com.ataulm.wutson.episodes.Episodes;
import com.ataulm.wutson.seasons.Season;
import com.ataulm.wutson.seasons.Seasons;
import com.ataulm.wutson.shows.Show;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.ShowSummaries;
import com.ataulm.wutson.shows.discover.ShowsInGenre;
import com.ataulm.wutson.shows.TrackedStatus;
import com.ataulm.wutson.shows.WatchedStatus;
import com.ataulm.wutson.shows.myshows.Watchlist;

import java.util.List;

import rx.Observable;

public interface DataRepository {

    Observable<ShowSummaries> getMyShows();

    Observable<Watchlist> getWatchlist();

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
