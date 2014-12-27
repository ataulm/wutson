package com.ataulm.wutson.trackedshows;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.R;

class TrackedShowsViewHolder extends RecyclerView.ViewHolder {

    private final ImageView posterImageView;
    private final TextView showTextView;
    private final TextView episodeNameTextView;

    public TrackedShowsViewHolder(View itemView) {
        super(itemView);
        posterImageView = (ImageView) itemView.findViewById(R.id.tracked_shows_image_poster);
        showTextView = (TextView) itemView.findViewById(R.id.tracked_shows_text_show);
        episodeNameTextView = (TextView) itemView.findViewById(R.id.tracked_shows_text_episode_name);
    }

    void bind(TrackedShow trackedShow) {
        itemView.setContentDescription(trackedShow.getShowName() + ": " + trackedShow.getNextEpisodeName());

        showTextView.setText(trackedShow.getShowName());
        episodeNameTextView.setText(trackedShow.getNextEpisodeName());
        // TODO: bind poster
    }

}
