package com.ataulm.wutson.myshows;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;
import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.view.ShowSummaryView;

final class ShowSummaryViewHolder extends RecyclerView.ViewHolder {

    static ShowSummaryViewHolder inflate(ViewGroup parent, LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.view_tracked_shows_item, parent, false);
        return new ShowSummaryViewHolder(view);
    }

    private ShowSummaryViewHolder(View itemView) {
        super(itemView);
    }

    void bind(final ShowSummary show, final OnShowClickListener listener) {
        ((ShowSummaryView) itemView).setTitle(show.getName());
        ((ShowSummaryView) itemView).setPoster(show.getPosterUri());
        ((ShowSummaryView) itemView).setPopupMenu(R.menu.menu_show_summary, new ShowSummaryView.OnMenuItemClickListener() {

            @Override
            public void onClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.show_summary_menu_item_stop_tracking:
                        listener.onClickStopTracking(show);
                        Snackbar.make(itemView, "Removed " + show.getName(), Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        listener.onClickTrack(show);
                                    }
                                })
                                .show();
                        return;
                    default:
                        throw new IllegalArgumentException("Unknown menuItem: " + menuItem.getTitle());
                }
            }

        });

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onClick(show);
            }

        });
    }

}
