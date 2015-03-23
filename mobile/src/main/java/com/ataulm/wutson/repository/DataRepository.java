package com.ataulm.wutson.repository;

import com.ataulm.wutson.discover.ShowsInGenre;
import com.ataulm.wutson.discover.ShowsInGenreRepository;
import com.ataulm.wutson.seasons.Seasons;
import com.ataulm.wutson.seasons.SeasonsRepository;
import com.ataulm.wutson.showdetails.Show;
import com.ataulm.wutson.showdetails.ShowRepository;
import com.ataulm.wutson.tmdb.TmdbApi;

import java.util.List;

import rx.Observable;

public class DataRepository {

    private final ShowsInGenreRepository showsInGenreRepository;
    private final ShowRepository showRepository;
    private final SeasonsRepository seasonsRepository;

    public DataRepository(TmdbApi api) {
        ConfigurationRepository configurationRepository = new ConfigurationRepository(api);

        this.showsInGenreRepository = new ShowsInGenreRepository(api, configurationRepository);
        this.showRepository = new ShowRepository(api, configurationRepository);
        this.seasonsRepository = new SeasonsRepository(api, showRepository, configurationRepository);
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

}
