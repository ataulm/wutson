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
import com.ataulm.wutson.showdetails.OnClickSeasonListener;
import com.ataulm.wutson.showdetails.SeasonsAdapter;
import com.ataulm.wutson.showdetails.view.Details;
import com.ataulm.wutson.showdetails.view.DetailsAdapter;
import com.ataulm.wutson.showdetails.view.DetailsViewHolder;
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

    private Show show;

    ShowDetailsPagerAdapter(Resources resources, OnClickSeasonListener onSeasonClickListener, LayoutInflater layoutInflater, URI showBackdropUri) {
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
        View view = layoutInflater.inflate(Page.OVERVIEW.getLayoutResId(), container, false);
        ImageView backdropImageView = (ImageView) view.findViewById(R.id.show_details_about_image_backdrop);
        Glide.with(backdropImageView.getContext())
                .load(showBackdropUri.toString())
                .into(backdropImageView);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.show_details_about_recycler);
        LinearLayoutManager layout = new LinearLayoutManager(recyclerView.getContext());
        layout.setStackFromEnd(true);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(new DetailsAdapter(getDetails(), layoutInflater));
        recyclerView.scrollToPosition(0);
        return view;
    }

    private Details getDetails() {
        if (show == null) {
            return new Details();
        } else {
            return Details.from(show);
        }
    }

    private View getShowSeasonsView(ViewGroup container) {
        List<Show.SeasonSummary> seasonSummaries;
        if (show == null) {
            seasonSummaries = Collections.emptyList();
        } else {
            seasonSummaries = show.getSeasonSummaries();
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
