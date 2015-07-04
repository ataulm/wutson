package com.ataulm.wutson.myshows;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ataulm.wutson.shows.myshows.Watchlist;

class WatchlistAdapter extends RecyclerView.Adapter<WatchlistItemViewHolder> {

    private final LayoutInflater layoutInflater;

    private Watchlist watchlist;

    WatchlistAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        setHasStableIds(true);
    }

    @Override
    public WatchlistItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return WatchlistItemViewHolder.inflate(parent, layoutInflater);
    }

    @Override
    public void onBindViewHolder(WatchlistItemViewHolder holder, int position) {
        holder.bind(watchlist.get(position));
    }

    @Override
    public long getItemId(int position) {
        return watchlist.get(position).getShowName().hashCode();
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

}
