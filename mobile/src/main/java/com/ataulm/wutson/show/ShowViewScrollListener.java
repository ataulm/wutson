package com.ataulm.wutson.show;

import android.animation.ArgbEvaluator;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.ataulm.wutson.R;

class ShowViewScrollListener implements ShowView.ScrollListener {

    private final Transitioner navigationIconColorTransitioner;
    private final Transitioner appBarBackgroundTransitioner;

    private final int showPaddingTop;

    static ShowViewScrollListener newInstance(Resources resources, Drawable navIcon, Drawable appBarBackground) {
        Transitioner navIconTransitioner = new NavigationIconColorTransitioner(navIcon, new ArgbEvaluator());
        Transitioner appBarBackgroundTransitioner = new AppBarBackgroundAlphaTransitioner(appBarBackground);
        int showPaddingTop = resources.getDimensionPixelSize(R.dimen.show_details_padding_top) - resources.getDimensionPixelSize(R.dimen.app_bar_height);
        return new ShowViewScrollListener(navIconTransitioner, appBarBackgroundTransitioner, showPaddingTop);
    }

    ShowViewScrollListener(Transitioner navIconTransitioner, Transitioner appBarBackgroundTransitioner, int showPaddingTop) {
        this.navigationIconColorTransitioner = navIconTransitioner;
        this.appBarBackgroundTransitioner = appBarBackgroundTransitioner;
        this.showPaddingTop = showPaddingTop;
    }

    @Override
    public void onShowViewScrolled(int scrollY) {
        float transitionState = calculateTransitionState(scrollY);
        navigationIconColorTransitioner.onTransitionStateChange(transitionState);
        appBarBackgroundTransitioner.onTransitionStateChange(transitionState);
    }

    private float calculateTransitionState(int scrollY) {
        int scrolled = showPaddingTop - scrollY;

        float alpha = (float) scrolled / showPaddingTop;
        if (alpha <= 0) {
            alpha = 0;
        }
        if (alpha > 1) {
            alpha = 1;
        }
        return alpha;
    }

}
