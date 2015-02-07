package com.ataulm.wutson.discover;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;

import java.util.List;

class DiscoverPagerAdapter extends PagerAdapter {

    private final LayoutInflater layoutInflater;
    private final List<ShowsInGenre> showsSeparatedByGenre;
    private final OnShowClickListener listener;

    public DiscoverPagerAdapter(LayoutInflater layoutInflater, List<ShowsInGenre> showsSeparatedByGenre, OnShowClickListener listener) {
        this.layoutInflater = layoutInflater;
        this.showsSeparatedByGenre = showsSeparatedByGenre;
        this.listener = listener;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        ShowsInGenreView view = (ShowsInGenreView) layoutInflater.inflate(R.layout.view_shows_in_genre, container, false);
        ShowsInGenre showsInGenre = showsSeparatedByGenre.get(position);
        view.update(showsInGenre, listener);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return showsSeparatedByGenre.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
