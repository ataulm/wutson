package com.ataulm.wutson.search;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.myshows.SearchTvResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

final class SearchResultsViewHolder extends RecyclerView.ViewHolder {

    private final View itemView;
    private final TextView nameTextView;
    private final ImageView posterImageView;
    private final View trackButton;
    private final View overlay;

    static SearchResultsViewHolder inflate(ViewGroup parent, LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.view_search_result, parent, false);
        TextView nameTextView = (TextView) view.findViewById(R.id.search_result_text_name);
        ImageView posterImageView = (ImageView) view.findViewById(R.id.search_result_image_poster);
        View trackButton = view.findViewById(R.id.search_result_button_track);
        View overlay = view.findViewById(R.id.search_result_overlay);
        return new SearchResultsViewHolder(view, nameTextView, posterImageView, trackButton, overlay);
    }

    private SearchResultsViewHolder(View itemView, TextView nameTextView, ImageView posterImageView, View trackButton, View overlay) {
        super(itemView);
        this.itemView = itemView;
        this.nameTextView = nameTextView;
        this.posterImageView = posterImageView;
        this.trackButton = trackButton;
        this.overlay = overlay;
    }

    void bind(final SearchTvResult searchTvResult, final OnClickSearchTvResult listener) {
        nameTextView.setText(searchTvResult.getName());
        loadImage(searchTvResult);
    }

    private final int defaultOverlayBackgroundColor = Color.BLACK;

    private void loadImage(final SearchTvResult searchTvResult) {
        overlay.setBackgroundColor(defaultOverlayBackgroundColor);
        overlay.setTag(R.id.shows_in_genre_show_id, searchTvResult.getId());

        posterImageView.setImageBitmap(null);
        Glide.with(posterImageView.getContext())
                .load(searchTvResult.getPosterUri().toString())
                .asBitmap()
                .placeholder(R.drawable.ic_brand_placeholder)
                .into(new BitmapImageViewTarget(posterImageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        Palette.from(resource).generate(new OnPaletteGeneratedListener(searchTvResult.getId()));
                    }
                });
    }

    private class OnPaletteGeneratedListener implements Palette.PaletteAsyncListener {

        private final ShowId id;

        public OnPaletteGeneratedListener(ShowId id) {
            this.id = id;
        }

        @Override
        public void onGenerated(Palette palette) {
            if (showIdHasChanged()) {
                return;
            }
            int generatedColor = palette.getDarkMutedColor(defaultOverlayBackgroundColor);
            overlay.setBackgroundColor(generatedColor);
        }

        private boolean showIdHasChanged() {
            return !id.equals(overlay.getTag(R.id.shows_in_genre_show_id));
        }

    }

}
