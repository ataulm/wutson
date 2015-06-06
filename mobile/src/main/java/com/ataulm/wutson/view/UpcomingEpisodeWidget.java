package com.ataulm.wutson.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.bumptech.glide.Glide;

import java.net.URI;

public class UpcomingEpisodeWidget extends LinearLayout {

    private ImageView posterImageView;
    private TextView showNameTextView;
    private TextView episodeNumberTextView;
    private TextView airDateTextView;
    private String imageUrl;

    public UpcomingEpisodeWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_upcoming_episode, this);
        setOrientation(HORIZONTAL);

        posterImageView = (ImageView) findViewById(R.id.upcoming_episode_image_poster);
        showNameTextView = (TextView) findViewById(R.id.upcoming_episode_text_show_name);
        episodeNumberTextView = (TextView) findViewById(R.id.upcoming_episode_text_episode_number);
        airDateTextView = (TextView) findViewById(R.id.upcoming_episode_text_air_date);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (posterImageView != null && imageUrl != null) {
            loadImage();
        }
    }

    private void loadImage() {
        posterImageView.setImageBitmap(null);
        Glide.with(getContext())
                .load(imageUrl)
                .crossFade()
                .centerCrop()
                .placeholder(R.drawable.ic_episode_placeholder)
                .error(R.drawable.ic_episode_placeholder)
                .into(posterImageView);
    }

    public void setPoster(URI uri) {
        this.imageUrl = uri.toString();
        if (getMeasuredWidth() != 0 && getMeasuredHeight() != 0) {
            return;
        }
        loadImage();
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
