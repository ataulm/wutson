package com.ataulm.wutson.myshows;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.episodes.Episode;
import com.ataulm.wutson.episodes.Episodes;
import com.ataulm.wutson.shows.myshows.WatchlistItem;
import com.ataulm.wutson.view.UpcomingEpisodeWidget;

import java.util.Arrays;
import java.util.List;

final class WatchlistItemViewHolder extends RecyclerView.ViewHolder {

    private final View itemView;
    private final TextView showNameTextView;
    private final List<UpcomingEpisodeWidget> upcomingEpisodeWidgets;

    public static WatchlistItemViewHolder inflate(ViewGroup viewGroup, LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.view_watchlist_item, viewGroup, false);
        TextView showNameTextView = ((TextView) view.findViewById(R.id.watchlist_item_text_show_name));
        List<UpcomingEpisodeWidget> upcomingEpisodesWidgets = Arrays.asList(
                ((UpcomingEpisodeWidget) view.findViewById(R.id.watchlist_item_episode_0)),
                ((UpcomingEpisodeWidget) view.findViewById(R.id.watchlist_item_episode_1)),
                ((UpcomingEpisodeWidget) view.findViewById(R.id.watchlist_item_episode_2)),
                ((UpcomingEpisodeWidget) view.findViewById(R.id.watchlist_item_episode_3)),
                ((UpcomingEpisodeWidget) view.findViewById(R.id.watchlist_item_episode_4))
        );
        return new WatchlistItemViewHolder(view, showNameTextView, upcomingEpisodesWidgets);
    }

    private WatchlistItemViewHolder(View itemView, TextView showNameTextView, List<UpcomingEpisodeWidget> upcomingEpisodesWidgets) {
        super(itemView);
        this.itemView = itemView;
        this.showNameTextView = showNameTextView;
        upcomingEpisodeWidgets = upcomingEpisodesWidgets;
    }

    void bind(WatchlistItem watchlistItem) {
        hideAllUpcomingEpisodeWidgets();

        showNameTextView.setText(watchlistItem.getShowName());
        bind(watchlistItem.getEpisodes());
    }

    private void bind(Episodes episodes) {
        for (int i = 0; i < upcomingEpisodeWidgets.size() && i < episodes.size(); i++) {
            UpcomingEpisodeWidget upcomingEpisodeWidget = upcomingEpisodeWidgets.get(i);
            Episode episode = episodes.get(i);
            upcomingEpisodeWidget.setAirDate(episode.getAirDate().toString());
            upcomingEpisodeWidget.setEpisodeNumber(episode.getSeasonEpisodeNumber().toString());
            upcomingEpisodeWidget.setPoster(episode.getStillPath());
            upcomingEpisodeWidget.setShowName(episode.getShowName());
            upcomingEpisodeWidget.setVisibility(View.VISIBLE);
        }
    }

    private void hideAllUpcomingEpisodeWidgets() {
        for (UpcomingEpisodeWidget upcomingEpisodeWidget : upcomingEpisodeWidgets) {
            upcomingEpisodeWidget.setVisibility(View.GONE);
        }
    }

}
