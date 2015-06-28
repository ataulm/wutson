package com.ataulm.wutson.myshows;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ataulm.wutson.R;

class ShowNameWatchlistItemViewHolder extends WatchlistItemViewHolder {

    static ShowNameWatchlistItemViewHolder inflate(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_show_name_item, parent, false);
        return new ShowNameWatchlistItemViewHolder(view);
    }

    private ShowNameWatchlistItemViewHolder(View itemView) {
        super(itemView);
    }

    void bind(String showName) {
        ((TextView) itemView).setText(showName);
    }

}
