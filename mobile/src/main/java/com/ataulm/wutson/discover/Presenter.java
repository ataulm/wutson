package com.ataulm.wutson.discover;

import android.animation.Animator;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ataulm.wutson.R;
import com.ataulm.wutson.ToastDisplayer;
import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.shows.discover.DiscoverShows;
import com.ataulm.wutson.view.SimpleAnimatorListener;
import com.novoda.landingstrip.LandingStrip;

final class Presenter {

    private static final float TAB_STRIP_ALPHA_SHOW = 1;
    private static final float TAB_STRIP_TRANSLATION_Y_SHOW = 0;
    private static final float TAB_STRIP_ALPHA_HIDE = 0;
    private static final int ACTIVITY_TRANSITION_DELAY_MILLIS = 100;

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
        viewPager.setVisibility(View.GONE);
    }

    public void onLoadStop() {
        loadingView.setVisibility(View.GONE);
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

    public void hideTabs(Runnable runnable) {
        animateHideTabs(runnable);
    }

    private void animateHideTabs(final Runnable onAnimationEndAction) {
        tabStrip.animate()
                .setStartDelay(0)
                .translationY(-tabStrip.getHeight())
                .alpha(TAB_STRIP_ALPHA_HIDE)
                .setListener(
                        new SimpleAnimatorListener() {

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                tabStrip.setVisibility(View.INVISIBLE);
                                onAnimationEndAction.run();
                            }

                        }
                );
    }

    public void showTabs() {
        if (tabStrip.getAlpha() == TAB_STRIP_ALPHA_SHOW) {
            tabStrip.setAlpha(TAB_STRIP_ALPHA_HIDE);
            tabStrip.setTranslationY(-tabStrip.getHeight());
        }

        tabStrip.animate()
                .setStartDelay(ACTIVITY_TRANSITION_DELAY_MILLIS)
                .translationY(TAB_STRIP_TRANSLATION_Y_SHOW)
                .alpha(TAB_STRIP_ALPHA_SHOW)
                .setListener(
                        new SimpleAnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                tabStrip.setVisibility(View.VISIBLE);
                            }

                        }
                );
    }

}
