package com.ataulm.wutson.show;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;

import java.net.URI;

class ShowPagerAdapter extends PagerAdapter {

    private final OnClickSeasonListener onSeasonClickListener;
    private final LayoutInflater layoutInflater;
    private final URI backdropUri;

    private Show show;

    ShowPagerAdapter(OnClickSeasonListener onSeasonClickListener, LayoutInflater layoutInflater, URI backdropUri) {
        this.onSeasonClickListener = onSeasonClickListener;
        this.layoutInflater = layoutInflater;
        this.backdropUri = backdropUri;
    }

    void update(Show show) {
        this.show = show;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        if (position == 0) {
            return instantiateShowOverviewPage(container);
        }
        if (position == 1) {
            return instantiateShowSeasonsPage(container);
        }
        throw DeveloperError.because("ShowView should only have two pages");
    }

    private View instantiateShowOverviewPage(ViewGroup container) {
        ShowOverviewView view = (ShowOverviewView) layoutInflater.inflate(R.layout.view_show_details_about_page, container, false);

        if (show == null) {
            view.setBackdrop(backdropUri);
            container.addView(view);
            return view;
        }

        view.setBackdrop(show.getBackdropUri());
        view.setOverview(show.getOverview());
        view.setCast(show.getCast());

        container.addView(view);
        return view;
    }

    private View instantiateShowSeasonsPage(ViewGroup container) {
        if (show == null || show.getSeasons().isEmpty()) {
            View view = layoutInflater.inflate(R.layout.view_show_details_seasons_page_empty, container, false);
            container.addView(view);
            return view;
        }

        RecyclerView view = (RecyclerView) layoutInflater.inflate(R.layout.view_show_details_seasons_page, container, false);
        view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        RecyclerView.Adapter seasonsAdapter = new SeasonsAdapter(layoutInflater, show.getSeasons(), onSeasonClickListener);
        view.setAdapter(seasonsAdapter);

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return 2;
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
        if (position == 0) {
            return "OVERVIEW";
        } else {
            return "SEASONS";
        }
    }

}
