package com.ataulm.wutson.show;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;

class ShowPagerAdapter extends PagerAdapter {

    private final OnClickSeasonListener onSeasonClickListener;
    private final LayoutInflater layoutInflater;
    private Show show;

    ShowPagerAdapter(OnClickSeasonListener onSeasonClickListener, LayoutInflater layoutInflater) {
        this.onSeasonClickListener = onSeasonClickListener;
        this.layoutInflater = layoutInflater;
    }

    void update(Show show) {
        this.show = show;
        notifyDataSetChanged();
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
        ShowOverviewView view = (ShowOverviewView) layoutInflater.inflate(R.layout.view_show_overview, container, false);
        view.display(show);
        container.addView(view);
        return view;
    }

    private View instantiateShowSeasonsPage(ViewGroup container) {
        RecyclerView view = (RecyclerView) layoutInflater.inflate(R.layout.view_show_seasons, container, false);
        view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        RecyclerView.Adapter seasonsAdapter = new SeasonsAdapter(layoutInflater, show.getSeasons(), onSeasonClickListener);
        view.setAdapter(seasonsAdapter);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if (show == null) {
            return 0;
        }

        if (show.getSeasons().isEmpty()) {
            return 1;
        } else {
            return 2;
        }
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
