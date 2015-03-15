package com.ataulm.wutson.show;

import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;

import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class ShowPagerAdapter extends PagerAdapter {

    private final Resources resources;
    private final OnClickSeasonListener onSeasonClickListener;
    private final LayoutInflater layoutInflater;
    private final URI showBackdropUri;

    private final Map<Page, View> views;

    private Show show;

    ShowPagerAdapter(Resources resources, OnClickSeasonListener onSeasonClickListener, LayoutInflater layoutInflater, URI showBackdropUri) {
        this.resources = resources;
        this.onSeasonClickListener = onSeasonClickListener;
        this.layoutInflater = layoutInflater;
        this.showBackdropUri = showBackdropUri;

        this.views = new HashMap<>(Page.values().length);
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        Page page = Page.from(position);
        View pageView = layoutInflater.inflate(page.getLayoutResId(), container, false);
        views.put(page, pageView);

        switch (page) {
            case OVERVIEW:
                updatePageOverview((ShowOverviewView) pageView);
                break;
            case SEASONS:
                updatePageSeasons((RecyclerView) pageView);
                break;
        }

        container.addView(pageView);
        return pageView;
    }

    void update(Show show) {
        this.show = show;

        if (views.containsKey(Page.OVERVIEW)) {
            ShowOverviewView view = (ShowOverviewView) views.get(Page.OVERVIEW);
            view.setOverview(show.getOverview());
            view.setCast(show.getCast());
        }

        if (views.containsKey(Page.SEASONS)) {
            RecyclerView seasonsView = (RecyclerView) views.get(Page.SEASONS);
            RecyclerView.Adapter seasonsAdapter = new SeasonsAdapter(layoutInflater, show.getSeasons(), onSeasonClickListener);
            seasonsView.setAdapter(seasonsAdapter);
        }

    }

    private void updatePageOverview(ShowOverviewView view) {
        view.setBackdrop(showBackdropUri);
        if (show == null) {
            return;
        }
        view.setOverview(show.getOverview());
        view.setCast(show.getCast());
    }

    private void updatePageSeasons(RecyclerView view) {
        view.setLayoutManager(new LinearLayoutManager(view.getContext()));
        if (show == null) {
            return;
        }

        RecyclerView.Adapter seasonsAdapter = new SeasonsAdapter(layoutInflater, show.getSeasons(), onSeasonClickListener);
        view.setAdapter(seasonsAdapter);
    }

    @Override
    public int getCount() {
        return Page.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        views.remove(Page.from(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return resources.getString(Page.from(position).getTitleResId()).toUpperCase(Locale.getDefault());
    }

    private enum Page {

        OVERVIEW(R.layout.view_show_details_about_page, R.string.show_details_pager_title_overview),
        SEASONS(R.layout.view_show_details_seasons_page, R.string.show_details_pager_title_seasons);

        private final int layoutResId;
        private final int titleResId;

        private Page(@LayoutRes int layoutResId, @StringRes int titleResId) {
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
