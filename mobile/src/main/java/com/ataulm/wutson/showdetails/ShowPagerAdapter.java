package com.ataulm.wutson.showdetails;

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
import com.ataulm.wutson.vpa.ViewPagerAdapter;

import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class ShowPagerAdapter extends ViewPagerAdapter {

    private final Resources resources;
    private final OnClickSeasonListener onSeasonClickListener;
    private final LayoutInflater layoutInflater;
    private final URI showBackdropUri;

    private final Map<Page, View> pageViews;

    private Show show;

    ShowPagerAdapter(Resources resources, OnClickSeasonListener onSeasonClickListener, LayoutInflater layoutInflater, URI showBackdropUri) {
        this.resources = resources;
        this.onSeasonClickListener = onSeasonClickListener;
        this.layoutInflater = layoutInflater;
        this.showBackdropUri = showBackdropUri;

        this.pageViews = new HashMap<>(Page.values().length);
    }

    @Override
    protected View getView(ViewGroup container, int position) {
        Page page = Page.from(position);
        View pageView = layoutInflater.inflate(page.getLayoutResId(), container, false);
        pageViews.put(page, pageView);

        switch (page) {
            case OVERVIEW:
                ((ShowOverviewView) pageView).setBackdrop(showBackdropUri);
                if (show != null) {
                    updateShowOverview();
                }
                break;
            case SEASONS:
                ((RecyclerView) pageView).setLayoutManager(new LinearLayoutManager(pageView.getContext()));
                if (show != null) {
                    updateShowSeasons();
                }
                break;
        }

        return pageView;
    }

    private void updateShowSeasons() {
        RecyclerView seasonsView = (RecyclerView) pageViews.get(Page.SEASONS);
        RecyclerView.Adapter seasonsAdapter = new SeasonsAdapter(layoutInflater, show.getSeasons(), onSeasonClickListener);
        seasonsAdapter.setHasStableIds(true);
        seasonsView.setAdapter(seasonsAdapter);
    }

    private void updateShowOverview() {
        ShowOverviewView view = (ShowOverviewView) pageViews.get(Page.OVERVIEW);
        view.setOverview(show.getOverview());
        view.setCast(show.getCast());
    }

    void update(Show show) {
        this.show = show;

        if (pageViews.containsKey(Page.OVERVIEW)) {
            updateShowOverview();
        }

        if (pageViews.containsKey(Page.SEASONS)) {
            updateShowSeasons();
        }
    }

    @Override
    public int getCount() {
        return Page.values().length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        pageViews.remove(Page.from(position));
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
