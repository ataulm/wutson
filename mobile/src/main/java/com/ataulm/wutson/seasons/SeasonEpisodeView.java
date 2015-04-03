package com.ataulm.wutson.seasons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.bumptech.glide.Glide;

public class SeasonEpisodeView extends RelativeLayout {

    private ImageView posterImageView;
    private TextView episodeNameTextView;
    private TextView episodeNumberTextView;

    public SeasonEpisodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_season_episode, this);
        posterImageView = (ImageView) findViewById(R.id.seasons_episode_image_poster);
        episodeNameTextView = (TextView) findViewById(R.id.season_episode_text_episode_name);
        episodeNumberTextView = (TextView) findViewById(R.id.season_episode_text_episode_air_date);
    }

    void display(Season.Episode episode) {
        Glide.with(getContext()).load(episode.getStillPath().toString()).into(posterImageView);
        episodeNameTextView.setText(episode.getName());
        episodeNumberTextView.setText(episode.getAirDate());
    }

}
