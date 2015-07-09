package com.ataulm.wutson.shows.myshows;

import com.ataulm.wutson.repository.ConfigurationRepository;
import com.ataulm.wutson.rx.Function;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.tmdb.Configuration;
import com.ataulm.wutson.tmdb.TmdbApi;
import com.ataulm.wutson.tmdb.gson.GsonSearchTvResult;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class SearchRepository {

    private final TmdbApi api;
    private final ConfigurationRepository configurationRepository;

    public SearchRepository(TmdbApi api, ConfigurationRepository configurationRepository) {
        this.api = api;
        this.configurationRepository = configurationRepository;
    }

    public Observable<SearchTvResults> searchFor(String query) {
        Observable<Configuration> configuration = configurationRepository.getConfiguration();
        Observable<GsonSearchTvResult> gsonSearchTvResult = api.getSearchTvResults(query).flatMap(Function.<GsonSearchTvResult>emitEachElement());

        return searchTvResultFrom(configuration, gsonSearchTvResult)
                .toList()
                .map(asSearchTvResults());
    }

    private static Observable<SearchTvResult> searchTvResultFrom(Observable<Configuration> configuration, Observable<GsonSearchTvResult> gsonSearchTvResult) {
        return Observable.combineLatest(configuration, gsonSearchTvResult, asSearchTvResult());
    }

    private static Func2<Configuration, GsonSearchTvResult, SearchTvResult> asSearchTvResult() {
        return new Func2<Configuration, GsonSearchTvResult, SearchTvResult>() {
            @Override
            public SearchTvResult call(Configuration configuration, GsonSearchTvResult gsonSearchTvResult) {
                return new SearchTvResult(
                        new ShowId(gsonSearchTvResult.id),
                        gsonSearchTvResult.name,
                        gsonSearchTvResult.overview,
                        configuration.completePoster(gsonSearchTvResult.posterPath),
                        configuration.completeBackdrop(gsonSearchTvResult.backdropPath)
                );
            }
        };
    }

    private static Func1<List<SearchTvResult>, SearchTvResults> asSearchTvResults() {
        return new Func1<List<SearchTvResult>, SearchTvResults>() {
            @Override
            public SearchTvResults call(List<SearchTvResult> searchTvResults) {
                return new SearchTvResults(searchTvResults);
            }
        };
    }

}
