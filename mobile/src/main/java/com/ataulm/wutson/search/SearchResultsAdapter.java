package com.ataulm.wutson.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ataulm.wutson.shows.myshows.SearchTvResult;

class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsViewHolder> {

    private final OnClickSearchTvResult listener;
    private final LayoutInflater layoutInflater;
    private DataSet<SearchTvResult> dataSet;

    SearchResultsAdapter(OnClickSearchTvResult listener, LayoutInflater layoutInflater) {
        this.listener = listener;
        this.layoutInflater = layoutInflater;
    }

    void update(DataSet<SearchTvResult> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public SearchResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return SearchResultsViewHolder.inflate(parent, layoutInflater);
    }

    @Override
    public void onBindViewHolder(SearchResultsViewHolder holder, int position) {
        SearchTvResult item = dataSet.getItem(position);
        holder.bind(item, listener);
    }

    @Override
    public long getItemId(int position) {
        return dataSet.getItemId(position);
    }

    @Override
    public int getItemCount() {
        if (dataSet == null) {
            return 0;
        }
        return dataSet.getItemCount();
    }

}
