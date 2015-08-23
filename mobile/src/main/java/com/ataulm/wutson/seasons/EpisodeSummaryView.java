package com.ataulm.wutson.seasons;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.episodes.Episode;
import com.ataulm.wutson.episodes.SeasonEpisodeNumber;
import com.bumptech.glide.Glide;

public class EpisodeSummaryView extends CardView {

    private ImageView posterImageView;
    private TextView episodeNumberTextView;
    private TextView episodeNameTextView;
    private TextView episodeAirDateTextView;

    public EpisodeSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_episode_summary, this);
        posterImageView = (ImageView) findViewById(R.id.episode_summary_image_poster);
        episodeNumberTextView = (TextView) findViewById(R.id.episode_summary_text_episode_number);
        episodeNameTextView = (TextView) findViewById(R.id.episode_summary_text_episode_name);
        episodeAirDateTextView = (TextView) findViewById(R.id.episode_summary_text_episode_air_date);
    }

    void display(Episode episode) {
        updateStillPoster(episode);
        updateEpisodeNumber(episode);
        updateAirDate(episode);
        updateEpisodeName(episode);
    }

    private void updateEpisodeNumber(Episode episode) {
        SeasonEpisodeNumber seasonEpisodeNumber = episode.getSeasonEpisodeNumber();
        if (seasonEpisodeNumber.getSeason() == 0) {
            episodeNumberTextView.setVisibility(GONE);
            episodeNameTextView.setPadding(
                    episodeNameTextView.getPaddingLeft(),
                    getResources().getDimensionPixelSize(R.dimen.episode_summary_name_padding_top_if_episode_number_not_present),
                    episodeNameTextView.getPaddingRight(),
                    episodeNameTextView.getPaddingBottom()
            );
        } else {
            episodeNumberTextView.setVisibility(VISIBLE);
            episodeNameTextView.setPadding(
                    episodeNameTextView.getPaddingLeft(),
                    getResources().getDimensionPixelSize(R.dimen.episode_summary_name_margin_top),
                    episodeNameTextView.getPaddingRight(),
                    episodeNameTextView.getPaddingBottom()
            );
        }

        String episodeNumber = getResources().getString(R.string.episode_summary_season_number_episode_number_format, seasonEpisodeNumber.getSeason(), seasonEpisodeNumber.getEpisode());
        episodeNumberTextView.setText(episodeNumber);
    }

    private void updateEpisodeName(Episode episode) {
        episodeNameTextView.setText(episode.getName());
    }

    private void updateStillPoster(Episode episode) {
        Glide.with(getContext())
                .load(episode.getStillPath().toString())
                .centerCrop()
                .error(R.drawable.ic_season_or_episode_placeholder)
                .into(posterImageView);
    }

    private void updateAirDate(Episode episode) {
        if (episode.getAirDate().isValid()) {
            episodeAirDateTextView.setText(episode.getAirDate().toString());
            episodeAirDateTextView.setVisibility(VISIBLE);
        } else {
            episodeAirDateTextView.setVisibility(GONE);
        }
    }

}
