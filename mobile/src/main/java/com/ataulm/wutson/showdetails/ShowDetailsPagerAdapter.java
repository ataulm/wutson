package com.ataulm.wutson.showdetails;

import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ataulm.rv.SpacesItemDecoration;
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

    private RecyclerView detailsRecyclerView;
    private RecyclerView seasonsRecyclerView;
    private Show show;

    ShowDetailsPagerAdapter(Resources resources, OnClickSeasonListener onSeasonClickListener, LayoutInflater layoutInflater, URI showBackdropUri) {
        this.resources = resources;
        this.onSeasonClickListener = onSeasonClickListener;
        this.layoutInflater = layoutInflater;
        this.showBackdropUri = showBackdropUri;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Page page = Page.at(position);
        return resources.getString(page.getTitleResId()).toUpperCase(Locale.getDefault());
    }

    @Override
    public int getCount() {
        return Page.values().length;
    }

    @Override
    protected View getView(ViewGroup container, int position) {
        View view;
        switch (Page.at(position)) {
            case DETAILS:
                view = createDetailsView(container);
                break;
            case SEASONS:
                view = createSeasonsView(container);
                break;
            default:
                throw DeveloperError.because("max " + Page.values().length + " page(s). Got request for page at position: " + position);
        }

        updateDetails();
        updateSeasons();

        return view;
    }

    private View createDetailsView(ViewGroup container) {
        View view = layoutInflater.inflate(Page.DETAILS.getLayoutResId(), container, false);
        ImageView backdropImageView = (ImageView) view.findViewById(R.id.show_details_about_image_backdrop);
        Glide.with(backdropImageView.getContext())
                .load(showBackdropUri.toString())
                .error(R.drawable.ic_hero_image_placeholder)
                .into(backdropImageView);

        detailsRecyclerView = (RecyclerView) view.findViewById(R.id.show_details_about_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(detailsRecyclerView.getContext());
        layoutManager.setStackFromEnd(true);
        detailsRecyclerView.setLayoutManager(layoutManager);
        detailsRecyclerView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        return view;
    }

    private void updateDetails() {
        if (detailsRecyclerView == null) {
            return;
        }

        if (detailsRecyclerView.getAdapter() == null) {
            DetailsAdapter detailsAdapter = new DetailsAdapter(layoutInflater);
            detailsAdapter.update(getDetails());
            detailsRecyclerView.setAdapter(detailsAdapter);
        } else {
            ((DetailsAdapter) detailsRecyclerView.getAdapter()).update(getDetails());
        }
        detailsRecyclerView.scrollToPosition(0);
    }

    private Details getDetails() {
        if (show == null) {
            return Details.empty();
        } else {
            return Details.from(show);
        }
    }

    private View createSeasonsView(ViewGroup container) {
        seasonsRecyclerView = (RecyclerView) layoutInflater.inflate(Page.SEASONS.getLayoutResId(), container, false);
        seasonsRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        int spanCount = 1;
        seasonsRecyclerView.addItemDecoration(
                SpacesItemDecoration.newInstance(
                        resources.getDimensionPixelSize(R.dimen.show_details_seasons_list_horizontal_spacing),
                        resources.getDimensionPixelSize(R.dimen.show_details_seasons_list_vertical_spacing),
                        spanCount
                )
        );
        return seasonsRecyclerView;
    }

    private void updateSeasons() {
        if (seasonsRecyclerView == null) {
            return;
        }

        if (seasonsRecyclerView.getAdapter() == null) {
            SeasonsAdapter seasonsAdapter = new SeasonsAdapter(layoutInflater, onSeasonClickListener);
            seasonsAdapter.update(getSeasonSummaries());
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

    void update(Show show) {
        this.show = show;
        updateDetails();
        updateSeasons();
    }

}
