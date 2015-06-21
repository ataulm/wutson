package com.ataulm.wutson.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;
import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.model.ShowsInGenre;
import com.ataulm.wutson.view.ShowSummaryView;

class ShowsInGenreAdapter extends RecyclerView.Adapter<ShowsInGenreAdapter.ShowSummaryViewHolder> {

    private final OnShowClickListener listener;
    private final LayoutInflater layoutInflater;
    private ShowsInGenre showsInGenre;

    ShowsInGenreAdapter(LayoutInflater layoutInflater, OnShowClickListener listener) {
        this.listener = listener;
        this.layoutInflater = layoutInflater;
    }

    void update(ShowsInGenre showsInGenre) {
        this.showsInGenre = showsInGenre;
        notifyDataSetChanged();
    }

    @Override
    public ShowSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ShowSummaryViewHolder.inflate(layoutInflater, parent, listener);
    }

    @Override
    public void onBindViewHolder(ShowSummaryViewHolder holder, int position) {
        ShowSummary showSummary = showsInGenre.get(position);
        holder.update(showSummary);
    }

    @Override
    public long getItemId(int position) {
        return showsInGenre.get(position).getId().hashCode();
    }

    @Override
    public int getItemCount() {
        return showsInGenre.size();
    }

    static final class ShowSummaryViewHolder extends RecyclerView.ViewHolder {

        private final ShowSummaryView itemView;
        private final OnShowClickListener listener;

        static ShowSummaryViewHolder inflate(LayoutInflater layoutInflater, ViewGroup parent, OnShowClickListener listener) {
            ShowSummaryView itemView = (ShowSummaryView) layoutInflater.inflate(R.layout.view_discover_shows_in_genre_item, parent, false);
            return new ShowSummaryViewHolder(itemView, listener);
        }

        private ShowSummaryViewHolder(ShowSummaryView itemView, OnShowClickListener listener) {
            super(itemView);
            this.itemView = itemView;
            this.listener = listener;
        }

        void update(final ShowSummary showSummary) {
            itemView.setPoster(showSummary.getPosterUri());
            itemView.setTitle(showSummary.getName());
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onClick(showSummary);
                }

            });
        }

    }

}
