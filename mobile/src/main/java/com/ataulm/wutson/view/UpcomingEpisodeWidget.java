package com.ataulm.wutson.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.bumptech.glide.Glide;

import java.net.URI;

public class UpcomingEpisodeWidget extends CardView {

    private ImageView posterImageView;
    private TextView showNameTextView;
    private TextView episodeNumberTextView;
    private TextView airDateTextView;

    public UpcomingEpisodeWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_upcoming_episode, this);

        posterImageView = (ImageView) findViewById(R.id.upcoming_episode_image_poster);
        showNameTextView = (TextView) findViewById(R.id.upcoming_episode_text_show_name);
        episodeNumberTextView = (TextView) findViewById(R.id.upcoming_episode_text_episode_number);
        airDateTextView = (TextView) findViewById(R.id.upcoming_episode_text_air_date);
    }

    public void setPoster(URI uri) {
        posterImageView.setImageBitmap(null);
        Glide.with(getContext())
                .load(uri.toString())
                .crossFade()
                .placeholder(R.drawable.ic_episode_placeholder)
                .error(R.drawable.ic_episode_placeholder)
                .into(posterImageView);
    }

    public void setShowName(String showName) {
        showNameTextView.setText(showName);
    }

    public void setEpisodeNumber(String episodeNumber) {
        episodeNumberTextView.setText(episodeNumber);
    }

    public void setAirDate(String airDate) {
        airDateTextView.setText(airDate);
    }

}
