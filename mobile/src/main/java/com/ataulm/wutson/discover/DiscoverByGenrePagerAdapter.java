package com.ataulm.wutson.discover;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;

import java.util.Collections;
import java.util.List;

class DiscoverByGenrePagerAdapter extends PagerAdapter {

    private final LayoutInflater layoutInflater;
    private final OnShowClickListener listener;

    private List<ShowsInGenre> showsInGenres;

    public DiscoverByGenrePagerAdapter(LayoutInflater layoutInflater, OnShowClickListener listener) {
        this.layoutInflater = layoutInflater;
        this.listener = listener;
        this.showsInGenres = Collections.emptyList();
    }

    void update(List<ShowsInGenre> showsInGenres) {
        this.showsInGenres = showsInGenres;
        notifyDataSetChanged();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        DiscoverByGenreView view = (DiscoverByGenreView) layoutInflater.inflate(R.layout.view_discover_by_genre, container, false);
        ShowsInGenre showsInGenre = showsInGenres.get(position);
        view.update(showsInGenre, listener);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return showsInGenres.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return showsInGenres.get(position).getGenre();
    }

}
