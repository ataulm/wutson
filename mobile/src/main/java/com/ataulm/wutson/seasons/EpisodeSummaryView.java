package com.ataulm.wutson.seasons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.episodes.Episode;
import com.bumptech.glide.Glide;

public class EpisodeSummaryView extends RelativeLayout {

    private ImageView posterImageView;
    private TextView episodeNameTextView;
    private TextView episodeNumberTextView;

    public EpisodeSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_episode_summary, this);
        posterImageView = (ImageView) findViewById(R.id.episode_summary_image_poster);
        episodeNameTextView = (TextView) findViewById(R.id.episode_summary_text_episode_name);
        episodeNumberTextView = (TextView) findViewById(R.id.episode_summary_text_episode_air_date);
    }

    void display(Episode episode) {
        updateStillPoster(episode);
        updateAirDate(episode);
    }

    private void updateStillPoster(Episode episode) {
        Glide.with(getContext())
                .load(episode.getStillPath().toString())
                .centerCrop()
                .error(R.drawable.ic_season_or_episode_placeholder)
                .into(posterImageView);
        episodeNameTextView.setText(episode.getName());
    }

    private void updateAirDate(Episode episode) {
        if (episode.getAirDate().isValid()) {
            episodeNumberTextView.setText(episode.getAirDate().toString());
            episodeNumberTextView.setVisibility(VISIBLE);
        } else {
            episodeNumberTextView.setVisibility(GONE);
        }
    }

}
