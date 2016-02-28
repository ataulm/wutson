package com.ataulm.wutson.search;

import com.ataulm.wutson.repository.event.Event;
import com.ataulm.wutson.rx.EventFunctions;
import com.ataulm.wutson.rx.Functions;
import com.ataulm.wutson.shows.myshows.SearchTvResult;
import com.ataulm.wutson.shows.myshows.SearchTvResults;
import com.ataulm.wutson.trakt.GsonSearchTvResult;
import com.ataulm.wutson.trakt.TraktApi;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

import static com.ataulm.wutson.rx.Functions.onlyNonNull;

public class SearchDataSource {

    private final TraktApi api;
    private final Converter converter;

    public SearchDataSource(TraktApi api, Converter converter) {
        this.api = api;
        this.converter = converter;
    }

    public Observable<Event<SearchTvResults>> query(String query) {
        return findTvSearchResultFor(query).compose(EventFunctions.<SearchTvResults>asEvents());
    }

    private Observable<SearchTvResults> findTvSearchResultFor(String query) {
        return api.getSearchTvResults(query)
                .flatMap(Functions.<GsonSearchTvResult>emitEachElement())
                .map(convertToSearchTvResult(converter))
                .filter(onlyNonNull())
                .toList()
                .map(collectAsSearchTvResults());
    }

    private static Func1<GsonSearchTvResult, SearchTvResult> convertToSearchTvResult(final Converter converter) {
        return new Func1<GsonSearchTvResult, SearchTvResult>() {
            @Override
            public SearchTvResult call(GsonSearchTvResult gsonSearchTvResult) {
                return converter.convert(gsonSearchTvResult);
            }
        };
    }

    private static Func1<List<SearchTvResult>, SearchTvResults> collectAsSearchTvResults() {
        return new Func1<List<SearchTvResult>, SearchTvResults>() {
            @Override
            public SearchTvResults call(List<SearchTvResult> searchTvResults) {
                return new SearchTvResults(searchTvResults);
            }
        };
    }

}
