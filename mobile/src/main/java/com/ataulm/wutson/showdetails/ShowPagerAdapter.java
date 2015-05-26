package com.ataulm.wutson.showdetails;

import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.vpa.ViewPagerAdapter;
import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;
import com.ataulm.wutson.model.Show;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

class ShowPagerAdapter extends ViewPagerAdapter {

    private final Resources resources;
    private final OnClickSeasonListener onSeasonClickListener;
    private final LayoutInflater layoutInflater;
    private final URI showBackdropUri;

    private Show show;

    ShowPagerAdapter(Resources resources, OnClickSeasonListener onSeasonClickListener, LayoutInflater layoutInflater, URI showBackdropUri) {
        this.resources = resources;
        this.onSeasonClickListener = onSeasonClickListener;
        this.layoutInflater = layoutInflater;
        this.showBackdropUri = showBackdropUri;
    }

    void update(Show show) {
        this.show = show;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return Page.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Page page = Page.from(position);
        return resources.getString(page.getTitleResId())
                .toUpperCase(Locale.getDefault());
    }

    @Override
    protected View getView(ViewGroup container, int position) {
        switch (Page.from(position)) {
            case OVERVIEW:
                return getShowView(container);
            case SEASONS:
                return getShowSeasonsView(container);
            default:
                throw DeveloperError.because("max " + Page.values().length + " page(s). Got request for page at position: " + position);
        }
    }

    private View getShowView(ViewGroup container) {
        ShowOverviewView view = (ShowOverviewView) layoutInflater.inflate(Page.OVERVIEW.getLayoutResId(), container, false);
        if (show != null) {
            view.setBackdrop(show.getBackdropUri());
            view.setCast(show.getCast());
            view.setOverview(show.getOverview());
        } else {
            view.setBackdrop(showBackdropUri);
        }
        return view;
    }

    private View getShowSeasonsView(ViewGroup container) {
        List<Show.SeasonSummary> seasonSummaries;
        if (show != null) {
            seasonSummaries = show.getSeasonSummaries();
        } else {
            seasonSummaries = Collections.emptyList();
        }

        RecyclerView view = (RecyclerView) layoutInflater.inflate(Page.SEASONS.getLayoutResId(), container, false);
        RecyclerView.Adapter seasonsAdapter = new SeasonsAdapter(layoutInflater, seasonSummaries, onSeasonClickListener);
        seasonsAdapter.setHasStableIds(true);
        view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        view.setAdapter(seasonsAdapter);
        return view;
    }

    private enum Page {

        OVERVIEW(R.layout.view_show_details_about_page, R.string.show_details_pager_title_overview),
        SEASONS(R.layout.view_show_details_seasons_page, R.string.show_details_pager_title_seasons);

        private final int layoutResId;
        private final int titleResId;

        Page(@LayoutRes int layoutResId, @StringRes int titleResId) {
            this.layoutResId = layoutResId;
            this.titleResId = titleResId;
        }

        int getLayoutResId() {
            return layoutResId;
        }

        int getTitleResId() {
            return titleResId;
        }

        static Page from(int position) {
            return values()[position];
        }

    }

}
