package com.ataulm.wutson.episodes;

import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ataulm.wutson.R;

public final class EpisodeNumberViewHolder extends DetailViewHolder {

    @ColorInt
    int accentColor;

    public static EpisodeNumberViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent, @ColorInt int accentColor) {
        View view = layoutInflater.inflate(R.layout.view_episode_details_item_episode_number, parent, false);
        return new EpisodeNumberViewHolder(view, accentColor);
    }

    private EpisodeNumberViewHolder(View itemView, @ColorInt int accentColor) {
        super(itemView);
        this.accentColor = accentColor;
    }

    @Override
    public void bind(Detail detail) {
        SeasonEpisodeNumber number = ((EpisodeNumberDetail) detail).getEpisodeNumber();
        String name = itemView.getResources().getString(R.string.episode_details_episode_number_format, number.getSeason(), number.getEpisode());
        ((TextView) itemView).setText(name);
        itemView.setBackgroundColor(accentColor);
    }

}
