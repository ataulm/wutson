package com.ataulm.wutson.myshows;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.model.Episode;

class WatchlistAdapter extends RecyclerView.Adapter<WatchlistItemViewHolder> {

    private Watchlist watchlist;

    @Override
    public WatchlistItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (ViewType.values()[viewType]) {
            case SHOW_NAME:
                return ShowNameWatchlistItemViewHolder.inflate(parent);
            case EPISODE:
                return UpcomingEpisodeViewHolder.inflate(parent);
            default:
                throw DeveloperError.because("unknown view type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(WatchlistItemViewHolder holder, int position) {
        ViewType viewType = ViewType.values()[getItemViewType(position)];
        switch (viewType) {
            case SHOW_NAME:
                String showName = (String) watchlist.get(position).getItem();
                ((ShowNameWatchlistItemViewHolder) holder).bind(showName);
                break;
            case EPISODE:
                Episode episode = (Episode) watchlist.get(position).getItem();
                ((UpcomingEpisodeViewHolder) holder).bind(episode);
                break;
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public int getItemViewType(int position) {
        WatchlistItem item = watchlist.get(position);
        return item.isShow() ? ViewType.SHOW_NAME.ordinal() : ViewType.EPISODE.ordinal();
    }

    @Override
    public int getItemCount() {
        if (watchlist == null) {
            return 0;
        }
        return watchlist.size();
    }

    public void update(Watchlist watchlist) {
        this.watchlist = watchlist;
        notifyDataSetChanged();
    }

    private enum ViewType {
        SHOW_NAME,
        EPISODE
    }

}
