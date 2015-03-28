package com.ataulm.wutson.repository;

import android.content.SharedPreferences;

import com.ataulm.wutson.discover.ShowsInGenre;
import com.ataulm.wutson.discover.ShowsInGenreRepository;
import com.ataulm.wutson.repository.persistence.PersistentDataRepository;
import com.ataulm.wutson.seasons.Seasons;
import com.ataulm.wutson.seasons.SeasonsRepository;
import com.ataulm.wutson.showdetails.Show;
import com.ataulm.wutson.showdetails.ShowRepository;
import com.ataulm.wutson.tmdb.TmdbApi;

import java.util.List;

import rx.Observable;

public class DataRepository {

    private final TrackedShowsRepository trackedShowsRepository;
    private final ShowsInGenreRepository showsInGenreRepository;
    private final ShowRepository showRepository;
    private final SeasonsRepository seasonsRepository;

    public DataRepository(TmdbApi api, SharedPreferences sharedPreferences, PersistentDataRepository persistentDataRepository) {
        ConfigurationRepository configurationRepository = new ConfigurationRepository(api, persistentDataRepository);

        this.showsInGenreRepository = new ShowsInGenreRepository(api, configurationRepository);
        this.showRepository = new ShowRepository(api, configurationRepository);
        this.seasonsRepository = new SeasonsRepository(api, showRepository, configurationRepository);
        this.trackedShowsRepository = TrackedShowsRepository.newInstance(sharedPreferences);
    }

    public Observable<Show> getShow(final String showId) {
        return showRepository.getShow(showId);
    }

    public Observable<List<ShowsInGenre>> getShowsSeparatedByGenre() {
        return showsInGenreRepository.getShowsSeparatedByGenre();
    }

    public Observable<Seasons> getSeasons(String showId) {
        return seasonsRepository.getSeasons(showId);
    }

    public Observable<Boolean> getTrackedStatusOfShowWith(String showId) {
        return trackedShowsRepository.getTrackedStatusOfShowWith(showId);
    }

    public void toggleTrackingShowWithId(String showId) {
        trackedShowsRepository.toggleTrackingShowWithId(showId);
    }

}
