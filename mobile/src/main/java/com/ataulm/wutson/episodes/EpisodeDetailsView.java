package com.ataulm.wutson.episodes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;
import com.ataulm.wutson.episodes.Episode;
import com.bumptech.glide.Glide;

public class EpisodeDetailsView extends LinearLayout {

    private TextView episodeNameTextView;

    public EpisodeDetailsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.setOrientation(VERTICAL);
        View.inflate(getContext(), R.layout.merge_episode_details, this);
        episodeNameTextView = (TextView) findViewById(R.id.episode_details_text_episode_name);
    }

    @Override
    public final void setOrientation(int orientation) {
        throw DeveloperError.methodCannotBeCalledOutsideThisClass();
    }

    void setEpisodeName(String episodeName) {
        episodeNameTextView.setText(episodeName);
    }

}
