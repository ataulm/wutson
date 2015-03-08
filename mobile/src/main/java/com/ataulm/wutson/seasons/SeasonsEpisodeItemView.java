package com.ataulm.wutson.seasons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.bumptech.glide.Glide;

public class SeasonsEpisodeItemView extends RelativeLayout {

    private ImageView posterImageView;
    private TextView episodeNumberTextView;
    private TextView episodeNameTextView;

    public SeasonsEpisodeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_seasons_episode_item, this);
        posterImageView = (ImageView) findViewById(R.id.season_episode_item_image_poster);
        episodeNumberTextView = (TextView) findViewById(R.id.season_episode_item_text_episode_number);
        episodeNameTextView = (TextView) findViewById(R.id.season_episode_item_text_episode_name);
    }

    void display(Season.Episode episode) {
        Glide.with(getContext()).load(episode.getStillPath().toString()).into(posterImageView);
        episodeNumberTextView.setText(String.valueOf(episode.getEpisodeNumber()));
        episodeNameTextView.setText(episode.getName());
    }

}
