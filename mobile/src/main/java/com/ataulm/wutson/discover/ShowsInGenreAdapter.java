package com.ataulm.wutson.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.model.ShowsInGenre;
import com.bumptech.glide.Glide;

public class ShowsInGenreAdapter extends RecyclerView.Adapter<ShowsInGenreAdapter.ShowSummaryViewHolder> {

    private final OnClickShowSummaryListener onClickShowSummaryListener;
    private final LayoutInflater layoutInflater;
    private ShowsInGenre showsInGenre;

    ShowsInGenreAdapter(LayoutInflater layoutInflater, OnClickShowSummaryListener onClickShowSummaryListener) {
        this.onClickShowSummaryListener = onClickShowSummaryListener;
        this.layoutInflater = layoutInflater;
    }

    void update(ShowsInGenre showsInGenre) {
        this.showsInGenre = showsInGenre;
        notifyDataSetChanged();
    }

    @Override
    public ShowSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ShowSummaryViewHolder.inflate(layoutInflater, parent, onClickShowSummaryListener);
    }

    @Override
    public void onBindViewHolder(ShowSummaryViewHolder holder, int position) {
        ShowSummary showSummary = showsInGenre.get(position);
        holder.update(showSummary);
    }

    @Override
    public long getItemId(int position) {
        return showsInGenre.get(position).getId().hashCode();
    }

    @Override
    public int getItemCount() {
        return showsInGenre.size();
    }

    static final class ShowSummaryViewHolder extends RecyclerView.ViewHolder {

        private final View itemView;
        private final ImageView posterImageView;
        private final TextView showNameTextView;
        private final View trackToggleView;

        private final OnClickShowSummaryListener onClickShowSummaryListener;

        static ShowSummaryViewHolder inflate(LayoutInflater layoutInflater, ViewGroup parent, OnClickShowSummaryListener onClickShowSummaryListener) {
            View itemView = layoutInflater.inflate(R.layout.view_discover_shows_in_genre_item, parent, false);
            ImageView posterImageView = (ImageView) itemView.findViewById(R.id.discover_shows_in_genre_item_image_poster);
            TextView showNameTextView = (TextView) itemView.findViewById(R.id.discover_shows_in_genre_item_text_show_name);
            View trackToggleView = itemView.findViewById(R.id.discover_shows_in_genre_item_view_track_toggle);
            return new ShowSummaryViewHolder(itemView, posterImageView, showNameTextView, trackToggleView, onClickShowSummaryListener);
        }

        private ShowSummaryViewHolder(View itemView, ImageView posterImageView, TextView showNameTextView, View trackToggleView, OnClickShowSummaryListener onClickShowSummaryListener) {
            super(itemView);
            this.itemView = itemView;
            this.posterImageView = posterImageView;
            this.showNameTextView = showNameTextView;
            this.trackToggleView = trackToggleView;
            this.onClickShowSummaryListener = onClickShowSummaryListener;
        }

        void update(final ShowSummary showSummary) {
            posterImageView.setImageBitmap(null);
            Glide.with(posterImageView.getContext())
                    .load(showSummary.getPosterUri().toString())
                    .into(posterImageView);

            showNameTextView.setText(showSummary.getName());
            trackToggleView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onClickShowSummaryListener.onClickToggleTrackedStatus(showSummary.getId());
                }

            });
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onClickShowSummaryListener.onClick(showSummary);
                }

            });
        }

    }

}
