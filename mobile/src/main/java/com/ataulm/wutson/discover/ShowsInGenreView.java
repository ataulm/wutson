package com.ataulm.wutson.discover;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ataulm.wutson.R;

public class ShowsInGenreView extends FrameLayout {

    private static final int SPAN_COUNT = 2;

    private Adapter adapter;

    public ShowsInGenreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowsInGenreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_shows_in_genre, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.shows_in_genre_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));

        adapter = new Adapter(LayoutInflater.from(getContext()));
        recyclerView.setAdapter(adapter);
    }

    void update(ShowsInGenre showsInGenre) {
        adapter.update(showsInGenre);
    }

    private static class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private final LayoutInflater layoutInflater;
        private ShowsInGenre showsInGenre;

        Adapter(LayoutInflater layoutInflater) {
            this.layoutInflater = layoutInflater;
        }

        void update(ShowsInGenre showsInGenre) {
            this.showsInGenre = showsInGenre;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.item_shows_in_genre, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Show show = showsInGenre.get(position);
            holder.update(show);
        }

        @Override
        public int getItemCount() {
            return showsInGenre.size();
        }

    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        void update(Show show) {
            ((TextView) itemView).setText(show.getName());
        }

    }

}
