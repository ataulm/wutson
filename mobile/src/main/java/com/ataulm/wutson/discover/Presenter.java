package com.ataulm.wutson.discover;

import android.support.v4.view.ViewPager;

import com.ataulm.wutson.R;
import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.shows.discover.DiscoverShows;
import com.novoda.landingstrip.LandingStrip;

final class Presenter {

    private final LandingStrip tabStrip;
    private final ViewPager viewPager;
    private final DiscoverShowsPagerAdapter adapter;

    static Presenter newInstance(final DiscoverActivity activity, final DiscoverNavigator navigator) {
        LandingStrip tabStrip = (LandingStrip) activity.findViewById(R.id.tab_strip);
        ViewPager viewPager = (ViewPager) activity.findViewById(R.id.discover_pager_genres);
        DiscoverShowSummaryInteractionListener interactionListener = new DiscoverShowSummaryInteractionListener() {

            @Override
            public void onClick(ShowSummary showSummary, int accentColor) {
                navigator.toShowDetails(showSummary.getId(), showSummary.getName(), showSummary.getBackdropUri().toString(), accentColor);
            }

        };
        DiscoverShowsPagerAdapter adapter = DiscoverShowsPagerAdapter.newInstance(activity, interactionListener, activity);
        return new Presenter(tabStrip, viewPager, adapter);
    }

    private Presenter(LandingStrip tabStrip, ViewPager viewPager, DiscoverShowsPagerAdapter adapter) {
        this.tabStrip = tabStrip;
        this.viewPager = viewPager;
        this.adapter = adapter;
    }

    public void present(DiscoverShows discoverShows) {
        adapter.update(discoverShows);

        if (viewPager.getAdapter() == null) {
            viewPager.setAdapter(adapter);
            tabStrip.attach(viewPager);
        }
    }

    public boolean showFirstPage() {
        if (viewPager.getCurrentItem() == 0) {
            return false;
        }
        viewPager.setCurrentItem(0);
        return true;
    }

}
