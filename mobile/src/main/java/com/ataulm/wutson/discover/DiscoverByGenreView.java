package com.ataulm.wutson.discover;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ataulm.wutson.R;

public class DiscoverByGenreView extends FrameLayout {

    private static final int SPAN_COUNT = 2;

    private Adapter adapter;
    private OnShowClickListener listener;

    public DiscoverByGenreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiscoverByGenreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_discover_by_genre, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.discover_by_genre_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        adapter = new Adapter(LayoutInflater.from(getContext()));
        recyclerView.setAdapter(adapter);
    }

    void update(ShowsInGenre showsInGenre, OnShowClickListener listener) {
        adapter.update(showsInGenre);
        this.listener = listener;
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

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
            View view = layoutInflater.inflate(R.layout.view_discover_by_genre_item, parent, false);
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

    private class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        void update(final Show show) {
            ((ShowsInGenreItemView) itemView).display(show);
            itemView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onClick(show);
                }

            });
        }

    }

}
