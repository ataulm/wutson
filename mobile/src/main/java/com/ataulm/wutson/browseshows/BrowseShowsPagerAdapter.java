package com.ataulm.wutson.browseshows;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;

import java.util.List;

class BrowseShowsPagerAdapter extends PagerAdapter {

    private final LayoutInflater layoutInflater;
    private final List<ShowsInGenre> showsSeparatedByGenre;

    public BrowseShowsPagerAdapter(LayoutInflater layoutInflater, List<ShowsInGenre> showsSeparatedByGenre) {
        this.layoutInflater = layoutInflater;
        this.showsSeparatedByGenre = showsSeparatedByGenre;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        ShowsInGenreView view = (ShowsInGenreView) layoutInflater.inflate(R.layout.view_shows_in_genre, container, false);
        ShowsInGenre showsInGenre = showsSeparatedByGenre.get(position);
        view.update(showsInGenre);
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
        // Override, do nothing.
    }

}
