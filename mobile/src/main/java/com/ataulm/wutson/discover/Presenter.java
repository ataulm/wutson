package com.ataulm.wutson.discover;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.ataulm.wutson.R;
import com.ataulm.wutson.ToastDisplayer;
import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.shows.discover.DiscoverShows;
import com.novoda.landingstrip.LandingStrip;

final class Presenter {

    private final LandingStrip tabStrip;
    private final View loadingView;
    private final ViewPager viewPager;
    private final DiscoverShowsPagerAdapter adapter;
    private final ToastDisplayer toaster;

    private DiscoverShows discoverShows;

    static Presenter newInstance(final DiscoverActivity activity, final DiscoverNavigator navigator) {
        LandingStrip tabStrip = (LandingStrip) activity.findViewById(R.id.tab_strip);
        View loadingView = activity.findViewById(R.id.loading);
        ViewPager viewPager = (ViewPager) activity.findViewById(R.id.discover_pager_genres);
        DiscoverShowSummaryInteractionListener interactionListener = new DiscoverShowSummaryInteractionListener() {

            @Override
            public void onClick(ShowSummary showSummary, int accentColor) {
                navigator.toShowDetails(showSummary.getId(), showSummary.getName(), showSummary.getBackdropUri().toString(), accentColor);
            }

        };
        DiscoverShowsPagerAdapter adapter = DiscoverShowsPagerAdapter.newInstance(activity, interactionListener, activity);
        ToastDisplayer toaster = new ToastDisplayer(activity);
        return new Presenter(tabStrip, loadingView, viewPager, adapter, toaster);
    }

    private Presenter(LandingStrip tabStrip, View loadingView, ViewPager viewPager, DiscoverShowsPagerAdapter adapter, ToastDisplayer toaster) {
        this.tabStrip = tabStrip;
        this.loadingView = loadingView;
        this.viewPager = viewPager;
        this.adapter = adapter;
        this.toaster = toaster;
    }

    public void onLoadStart() {
        loadingView.setVisibility(View.VISIBLE);
        tabStrip.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
    }

    public void onLoadStop() {
        loadingView.setVisibility(View.GONE);
        tabStrip.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
    }

    public void present(DiscoverShows discoverShows) {
        if (discoverShows == null || discoverShows.equals(this.discoverShows)) {
            return;
        }

        this.discoverShows = discoverShows;
        adapter.update(discoverShows);

        if (viewPager.getAdapter() == null) {
            viewPager.setAdapter(adapter);
            tabStrip.attach(viewPager);
        }
    }

    public void onError(Throwable error) {
        // RETRO: don't toast and be selective about which errors (and in which cases) to notify user about
        toaster.display("Something went wrong");
    }

    /**
     * @return true if page changed, else false
     */
    public boolean showFirstPage() {
        if (viewPager.getCurrentItem() == 0) {
            return false;
        }
        viewPager.setCurrentItem(0);
        return true;
    }

}
