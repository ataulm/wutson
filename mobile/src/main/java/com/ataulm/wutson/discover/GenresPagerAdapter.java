package com.ataulm.wutson.discover;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.vpa.ViewPagerAdapter;
import com.ataulm.wutson.R;

import java.util.Collections;
import java.util.List;

class GenresPagerAdapter extends ViewPagerAdapter {

    private final LayoutInflater layoutInflater;
    private final OnShowClickListener listener;

    private List<ShowsInGenre> showsInGenres;

    GenresPagerAdapter(LayoutInflater layoutInflater, OnShowClickListener listener) {
        this.layoutInflater = layoutInflater;
        this.listener = listener;
        this.showsInGenres = Collections.emptyList();
    }

    void update(List<ShowsInGenre> showsInGenres) {
        this.showsInGenres = showsInGenres;
        Collections.sort(this.showsInGenres);
        notifyDataSetChanged();
    }

    @Override
    public View getView(ViewGroup container, int position) {
        RecyclerView view = createView(container);
        bind(view, showsInGenres.get(position));
        return view;
    }

    private RecyclerView createView(ViewGroup parent) {
        RecyclerView showSummaryRecyclerView = (RecyclerView) layoutInflater.inflate(R.layout.view_discover_genres_page, parent, false);
        int spanCount = parent.getResources().getInteger(R.integer.discover_genres_page_span_count);
        showSummaryRecyclerView.setLayoutManager(new GridLayoutManager(parent.getContext(), spanCount));

        return showSummaryRecyclerView;
    }

    private void bind(RecyclerView view, ShowsInGenre showsInGenre) {
        ShowsInGenreAdapter adapter = new ShowsInGenreAdapter(layoutInflater, listener);
        adapter.setHasStableIds(true);
        adapter.update(showsInGenre);
        view.setAdapter(adapter);
    }

    @Override
    public int getCount() {
        return showsInGenres.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return showsInGenres.get(position).getGenre();
    }

}
