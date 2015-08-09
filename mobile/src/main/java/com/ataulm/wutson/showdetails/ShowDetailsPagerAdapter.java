package com.ataulm.wutson.showdetails;

import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ataulm.vpa.ViewPagerAdapter;
import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;
import com.ataulm.wutson.showdetails.view.Details;
import com.ataulm.wutson.showdetails.view.DetailsAdapter;
import com.ataulm.wutson.shows.Show;
import com.bumptech.glide.Glide;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

class ShowDetailsPagerAdapter extends ViewPagerAdapter {

    private final Resources resources;
    private final OnClickSeasonListener onSeasonClickListener;
    private final LayoutInflater layoutInflater;
    private final URI showBackdropUri;

    private RecyclerView overviewRecyclerView;
    private RecyclerView seasonsRecyclerView;

    private Show show;

    ShowDetailsPagerAdapter(Resources resources, OnClickSeasonListener onSeasonClickListener, LayoutInflater layoutInflater, URI showBackdropUri) {
        this.resources = resources;
        this.onSeasonClickListener = onSeasonClickListener;
        this.layoutInflater = layoutInflater;
        this.showBackdropUri = showBackdropUri;
    }

    void update(Show show) {
        this.show = show;
        updateOverview();
        updateSeasons();
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
        View view = layoutInflater.inflate(Page.OVERVIEW.getLayoutResId(), container, false);
        ImageView backdropImageView = (ImageView) view.findViewById(R.id.show_details_about_image_backdrop);
        Glide.with(backdropImageView.getContext())
                .load(showBackdropUri.toString())
                .into(backdropImageView);

        overviewRecyclerView = (RecyclerView) view.findViewById(R.id.show_details_about_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(overviewRecyclerView.getContext());
        linearLayoutManager.setStackFromEnd(true);
        overviewRecyclerView.setLayoutManager(linearLayoutManager);

        updateOverview();
        return view;
    }

    private void updateOverview() {
        if (overviewRecyclerView == null) {
            return;
        }

        if (overviewRecyclerView.getAdapter() == null) {
            overviewRecyclerView.setAdapter(new DetailsAdapter(layoutInflater));
        } else {
            ((DetailsAdapter) overviewRecyclerView.getAdapter()).update(getDetails());
        }
        overviewRecyclerView.scrollToPosition(0);
    }

    private Details getDetails() {
        if (show == null) {
            return Details.empty();
        } else {
            return Details.from(show);
        }
    }

    private View getShowSeasonsView(ViewGroup container) {
        seasonsRecyclerView = (RecyclerView) layoutInflater.inflate(Page.SEASONS.getLayoutResId(), container, false);
        seasonsRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        updateSeasons();
        return seasonsRecyclerView;
    }

    private void updateSeasons() {
        if (seasonsRecyclerView == null) {
            return;
        }

        if (seasonsRecyclerView.getAdapter() == null) {
            RecyclerView.Adapter seasonsAdapter = new SeasonsAdapter(layoutInflater, onSeasonClickListener);
            seasonsAdapter.setHasStableIds(true);
            seasonsRecyclerView.setAdapter(seasonsAdapter);
        } else {
            ((SeasonsAdapter) seasonsRecyclerView.getAdapter()).update(getSeasonSummaries());
        }
    }

    private List<Show.SeasonSummary> getSeasonSummaries() {
        if (show == null) {
            return Collections.emptyList();
        } else {
            return show.getSeasonSummaries();
        }
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
