package com.ataulm.wutson.shows.myshows;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SearchTvResults implements Iterable<SearchTvResult> {

    private final List<SearchTvResult> searchTvResults;

    public SearchTvResults(List<SearchTvResult> searchTvResults) {
        this.searchTvResults = Collections.unmodifiableList(searchTvResults);
    }

    public int size() {
        return searchTvResults.size();
    }

    public SearchTvResult get(int location) {
        return searchTvResults.get(location);
    }

    @Override
    public Iterator<SearchTvResult> iterator() {
        return searchTvResults.iterator();
    }

}
