package com.ataulm.wutson.episodes;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;
import com.ataulm.wutson.episodes.Episode;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.net.URI;

public class EpisodeDetailsView extends ScrollView {

    private ImageView episodePosterView;
    private TextView episodeNameTextView;
    private TextView episodeNumberTextView;
    private TextView episodeDescriptionTextView;




    public EpisodeDetailsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_episode_details, this);
        episodePosterView = (ImageView) findViewById(R.id.episode_details_image_episode_poster);
        episodeNameTextView = (TextView) findViewById(R.id.episode_details_text_episode_name);
        episodeNumberTextView = (TextView) findViewById(R.id.episode_details_text_episode_number);
        episodeDescriptionTextView = (TextView) findViewById(R.id.episode_details_text_episode_description);
    }


    void setEpisodeName(String episodeName) {
        episodeNameTextView.setText(episodeName);
    }

    void setEpisodePoster(URI uri) {
        episodePosterView.setImageBitmap(null);

        Glide.with(getContext())
                .load(uri.toString())
                .into(episodePosterView);
    }

    public void setEpisodeDescription(String overview) {
        episodeDescriptionTextView.setText(overview);

    }

    public void setEpisodeNumber(int seasonNumber, int episodeNumber) {
        episodeNumberTextView.setText("Series " + seasonNumber + " Episode " + episodeNumber);

    }
}
