package com.ataulm.wutson.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;
import com.ataulm.wutson.model.ShowSummary;

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
        ShowSummaryView view = (ShowSummaryView) layoutInflater.inflate(R.layout.view_discover_shows_in_genre_item, parent, false);
        return new ShowSummaryViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ShowSummaryViewHolder holder, int position) {
        ShowSummary showSummary = showsInGenre.get(position);
        holder.update(showSummary);
    }

    @Override
    public int getItemCount() {
        return showsInGenre.size();
    }

    static class ShowSummaryViewHolder extends RecyclerView.ViewHolder {

        private final ShowSummaryView showSummaryView;
        private final OnShowClickListener listener;

        ShowSummaryViewHolder(ShowSummaryView itemView, OnShowClickListener listener) {
            super(itemView);
            this.showSummaryView = itemView;
            this.listener = listener;
        }

        void update(final ShowSummary showSummary) {
            showSummaryView.setPoster(showSummary.getPosterUri().toString());
            showSummaryView.setTitle(showSummary.getName());
            showSummaryView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onClick(showSummary);
                }

            });
        }

    }

}
