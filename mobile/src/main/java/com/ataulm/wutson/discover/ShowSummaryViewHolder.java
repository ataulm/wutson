package com.ataulm.wutson.discover;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.ShowSummary;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

final class ShowSummaryViewHolder extends RecyclerView.ViewHolder {

    private final View itemView;
    private final View overlay;
    private final ImageView posterImageView;
    private final TextView showNameTextView;
    private final View trackToggleView;

    private final OnClickShowSummaryListener onClickShowSummaryListener;
    private final int defaultOverlayBackgroundColor;

    static ShowSummaryViewHolder inflate(LayoutInflater layoutInflater, ViewGroup parent, OnClickShowSummaryListener onClickShowSummaryListener) {
        View itemView = layoutInflater.inflate(R.layout.view_discover_shows_in_genre_item, parent, false);
        View overlay = itemView.findViewById(R.id.discover_shows_in_genre_item_overlay);
        ImageView posterImageView = (ImageView) itemView.findViewById(R.id.discover_shows_in_genre_item_image_poster);
        TextView showNameTextView = (TextView) itemView.findViewById(R.id.discover_shows_in_genre_item_text_show_name);
        View trackToggleView = itemView.findViewById(R.id.discover_shows_in_genre_item_view_track_toggle);
        int defaultOverlayBackgroundColor = parent.getResources().getColor(R.color.show_summary_title_background);

        return new ShowSummaryViewHolder(itemView, overlay, posterImageView, showNameTextView, trackToggleView, onClickShowSummaryListener, defaultOverlayBackgroundColor);
    }

    private ShowSummaryViewHolder(View itemView, View overlay, ImageView posterImageView, TextView showNameTextView, View trackToggleView, OnClickShowSummaryListener onClickShowSummaryListener, int defaultOverlayBackgroundColor) {
        super(itemView);
        this.itemView = itemView;
        this.overlay = overlay;
        this.posterImageView = posterImageView;
        this.showNameTextView = showNameTextView;
        this.trackToggleView = trackToggleView;
        this.onClickShowSummaryListener = onClickShowSummaryListener;
        this.defaultOverlayBackgroundColor = defaultOverlayBackgroundColor;
    }

    void update(final ShowSummary showSummary) {
        loadImage(showSummary);

        showNameTextView.setText(showSummary.getName());
        trackToggleView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickShowSummaryListener.onClickToggleTrackedStatus(showSummary.getId());
            }

        });
        // TODO: this should change depending on whether show is tracked
        String trackToggleContentDescription = trackToggleView.getResources().getString(R.string.discover_show_summary_start_track_show_content_description, showSummary.getName());
        trackToggleView.setContentDescription(trackToggleContentDescription);
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickShowSummaryListener.onClick(showSummary);
            }

        });
    }

    private void loadImage(final ShowSummary showSummary) {
        overlay.setBackgroundColor(defaultOverlayBackgroundColor);
        overlay.setTag(R.id.shows_in_genre_show_id, showSummary.getId());

        posterImageView.setImageBitmap(null);
        Glide.with(posterImageView.getContext())
                .load(showSummary.getPosterUri().toString())
                .asBitmap()
                .into(new BitmapImageViewTarget(posterImageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        Palette.from(resource).generate(new OnPaletteGeneratedListener(showSummary.getId()));
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
