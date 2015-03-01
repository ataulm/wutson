package com.ataulm.wutson.show;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.bumptech.glide.Glide;

public class ShowSeasonsItemView extends RelativeLayout {

    private ImageView posterImageView;
    private TextView seasonNumberTextView;
    private TextView episodeCountTextView;

    public ShowSeasonsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_show_seasons_item, this);
        posterImageView = (ImageView) findViewById(R.id.show_seasons_item_image_poster);
        seasonNumberTextView = (TextView) findViewById(R.id.show_seasons_item_text_season_number);
        episodeCountTextView = (TextView) findViewById(R.id.show_seasons_item_text_episode_count);
    }

    void display(Show.Season season) {
        Glide.with(getContext()).load(season.getPosterPath().toString()).into(posterImageView);

        if (season.getSeasonNumber() == 0) {
            seasonNumberTextView.setText(R.string.show_seasons_item_season_number_0);
        } else {
            String seasonNumberText = getResources().getString(R.string.show_seasons_item_season_number_format, season.getSeasonNumber());
            seasonNumberTextView.setText(seasonNumberText);
        }

        String episodeCountText = getResources().getString(R.string.show_seasons_item_episode_count_format, season.getEpisodeCount());
        episodeCountTextView.setText(episodeCountText);
    }

}
