package com.ataulm.wutson.search;

import com.ataulm.wutson.repository.event.Event;
import com.ataulm.wutson.rx.EventFunctions;
import com.ataulm.wutson.rx.Functions;
import com.ataulm.wutson.shows.myshows.SearchResult;
import com.ataulm.wutson.shows.myshows.SearchResults;
import com.ataulm.wutson.trakt.GsonSearchResult;
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

    public Observable<Event<SearchResults>> getSearchResultsEvents(String query) {
        return getSearchResults(query).compose(EventFunctions.<SearchResults>asEvents());
    }

    private Observable<SearchResults> getSearchResults(String query) {
        return api.getSearchResults(query)
                .flatMap(Functions.<GsonSearchResult>emitEachElement())
                .map(convertToSearchResult(converter))
                .filter(onlyNonNull())
                .toList()
                .map(collectAsSearchResults());
    }

    private static Func1<GsonSearchResult, SearchResult> convertToSearchResult(final Converter converter) {
        return new Func1<GsonSearchResult, SearchResult>() {

            @Override
            public SearchResult call(GsonSearchResult gsonSearchResult) {
                return converter.convert(gsonSearchResult);
            }

        };
    }

    private static Func1<List<SearchResult>, SearchResults> collectAsSearchResults() {
        return new Func1<List<SearchResult>, SearchResults>() {

            @Override
            public SearchResults call(List<SearchResult> searchResults) {
                return new SearchResults(searchResults);
            }

        };
    }

}
