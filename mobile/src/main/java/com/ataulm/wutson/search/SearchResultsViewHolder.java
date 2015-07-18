package com.ataulm.wutson.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.shows.myshows.SearchTvResult;

final class SearchResultsViewHolder extends RecyclerView.ViewHolder {

    private final TextView nameTextView;

    static SearchResultsViewHolder inflate(ViewGroup parent, LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.view_search_result, parent, false);
        TextView nameTextView = (TextView) view.findViewById(R.id.search_result_text_name);
        return new SearchResultsViewHolder(view, nameTextView);
    }

    private SearchResultsViewHolder(View itemView, TextView nameTextView) {
        super(itemView);
        this.nameTextView = nameTextView;
    }

    void bind(final SearchTvResult searchTvResult, final OnClickSearchTvResult listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(searchTvResult);
            }
        });
        nameTextView.setText(searchTvResult.getName());
    }

}
