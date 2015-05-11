package com.ataulm.wutson.myshows;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;
import com.ataulm.wutson.ToastDisplayer;
import com.ataulm.wutson.core.model.ShowSummary;
import com.ataulm.wutson.discover.OnShowClickListener;
import com.ataulm.wutson.view.ShowSummaryView;

final class ShowSummaryViewHolder extends RecyclerView.ViewHolder {

    private final ToastDisplayer toaster;

    static ShowSummaryViewHolder inflate(ViewGroup parent, ToastDisplayer toaster) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_tracked_shows_item, parent, false);
        return new ShowSummaryViewHolder(view, toaster);
    }

    private ShowSummaryViewHolder(View itemView, ToastDisplayer toaster) {
        super(itemView);
        this.toaster = toaster;
    }

    void bind(final ShowSummary show, final OnShowClickListener listener) {
        ((ShowSummaryView) itemView).setTitle(show.getName());
        ((ShowSummaryView) itemView).setPoster(show.getPosterUri());
        ((ShowSummaryView) itemView).setPopupMenu(R.menu.menu_show_summary, new ShowSummaryView.OnMenuItemClickListener() {

            @Override
            public void onClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.show_summary_menu_item_toggle_favourite:
                        onClickToggleFavourite(menuItem);
                        return;
                    case R.id.show_summary_menu_item_stop_tracking:
                        toaster.display("Stopped tracking " + show.getName());
                        return;
                    default:
                        throw new IllegalArgumentException("Unknown menuItem: " + menuItem.getTitle());
                }
            }

            private void onClickToggleFavourite(MenuItem menuItem) {
                if (menuItem.isChecked()) {
                    toaster.display("Removed favourite for  " + show.getName());
                    menuItem.setTitle("Add favourite");
                    menuItem.setChecked(false);
                } else {
                    toaster.display("Favourited " + show.getName() + "!");
                    menuItem.setTitle("Remove favourite");
                    menuItem.setChecked(true);
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
