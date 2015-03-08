package com.ataulm.wutson.repository;

import com.ataulm.wutson.discover.ShowsInGenre;
import com.ataulm.wutson.discover.ShowsInGenreRepository;
import com.ataulm.wutson.show.Show;
import com.ataulm.wutson.show.ShowRepository;
import com.ataulm.wutson.tmdb.TmdbApi;

import java.util.List;

import rx.Observable;

public class DataRepository {

    private final ShowsInGenreRepository showsInGenreRepository;
    private final ShowRepository showRepository;

    public DataRepository(TmdbApi api) {
        ConfigurationRepository configurationRepository = new ConfigurationRepository(api);

        this.showsInGenreRepository = new ShowsInGenreRepository(api, configurationRepository);
        this.showRepository = new ShowRepository(api, configurationRepository);
    }

    public Observable<Show> getShow(final String showId) {
        return showRepository.getShow(showId);
    }

    public Observable<List<ShowsInGenre>> getShowsSeparatedByGenre() {
        return showsInGenreRepository.getShowsSeparatedByGenre();
    }

}
