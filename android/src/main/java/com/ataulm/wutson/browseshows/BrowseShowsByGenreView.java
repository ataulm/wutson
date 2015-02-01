package com.ataulm.wutson.browseshows;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.model.DiscoverTvShows;

public class BrowseShowsByGenreView extends FrameLayout {

    private Adapter adapter;

    public BrowseShowsByGenreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BrowseShowsByGenreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_browse_shows_by_genre, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.browse_shows_by_genre_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new Adapter(LayoutInflater.from(getContext()));
        recyclerView.setAdapter(adapter);
    }

    void update(DiscoverTvShows discoverTvShows) {
        adapter.update(discoverTvShows);
    }

    private static class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private final LayoutInflater layoutInflater;
        private DiscoverTvShows discoverTvShows;

        Adapter(LayoutInflater layoutInflater) {
            this.layoutInflater = layoutInflater;
        }

        void update(DiscoverTvShows discoverTvShows) {
            this.discoverTvShows = discoverTvShows;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.view_browse_shows_by_genre_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DiscoverTvShows.Show show = discoverTvShows.get(position);
            holder.update(show);
        }

        @Override
        public int getItemCount() {
            return discoverTvShows.size();
        }

    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        void update(DiscoverTvShows.Show show) {
            ((TextView) itemView).setText(show.toString());
        }

    }

}
