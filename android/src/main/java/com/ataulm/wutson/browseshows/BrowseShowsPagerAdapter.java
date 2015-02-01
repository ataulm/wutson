package com.ataulm.wutson.browseshows;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;
import com.ataulm.wutson.model.DiscoverTvShows;

import java.util.List;

class BrowseShowsPagerAdapter extends PagerAdapter {

    private final LayoutInflater layoutInflater;
    private final List<DiscoverTvShows> discoverTvShowsList;

    public BrowseShowsPagerAdapter(LayoutInflater layoutInflater, List<DiscoverTvShows> discoverTvShowsList) {
        this.layoutInflater = layoutInflater;
        this.discoverTvShowsList = discoverTvShowsList;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        BrowseShowsByGenreView view = (BrowseShowsByGenreView) layoutInflater.inflate(R.layout.view_browse_shows_genre, container, false);
        DiscoverTvShows discoverTvShows = discoverTvShowsList.get(position);
        view.update(discoverTvShows);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return discoverTvShowsList.size();
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
