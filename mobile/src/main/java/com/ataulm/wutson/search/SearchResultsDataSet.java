package com.ataulm.wutson.search;

import com.ataulm.wutson.shows.myshows.SearchTvResult;
import com.ataulm.wutson.shows.myshows.SearchTvResults;

class SearchResultsDataSet implements DataSet<SearchTvResult> {

    private final SearchTvResults results;

    SearchResultsDataSet(SearchTvResults results) {
        this.results = results;
    }

    @Override
    public int getItemCount() {
        return results == null ? 0 : results.size();
    }

    @Override
    public SearchTvResult getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId().hashCode();
    }

}
