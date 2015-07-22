package com.ataulm.wutson.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.shows.myshows.SearchTvResult;

final class SearchResultsViewHolder extends RecyclerView.ViewHolder {

    private final View itemView;
    private final TextView nameTextView;
    private final ImageView posterImageView;
    private final View trackButton;

    static SearchResultsViewHolder inflate(ViewGroup parent, LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.view_search_result, parent, false);
        TextView nameTextView = (TextView) view.findViewById(R.id.search_result_text_name);
        ImageView posterImageView = (ImageView) view.findViewById(R.id.search_result_image_poster);
        View trackButton = view.findViewById(R.id.search_result_button_track);
        return new SearchResultsViewHolder(view, nameTextView, posterImageView, trackButton);
    }

    private SearchResultsViewHolder(View itemView, TextView nameTextView, ImageView posterImageView, View trackButton) {
        super(itemView);
        this.itemView = itemView;
        this.nameTextView = nameTextView;
        this.posterImageView = posterImageView;
        this.trackButton = trackButton;
    }

    void bind(final SearchTvResult searchTvResult, final OnClickSearchTvResult listener) {
        nameTextView.setText(searchTvResult.getName());
        trackButton.setVisibility(View.GONE);
    }

}
