package com.ataulm.wutson.shows.myshows;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SearchResults implements Iterable<SearchResult> {

    private final List<SearchResult> searchResults;

    public SearchResults(List<SearchResult> searchResults) {
        this.searchResults = Collections.unmodifiableList(searchResults);
    }

    public int size() {
        return searchResults.size();
    }

    public SearchResult get(int location) {
        return searchResults.get(location);
    }

    @Override
    public Iterator<SearchResult> iterator() {
        return searchResults.iterator();
    }

}
