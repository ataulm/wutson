package com.ataulm.wutson.show;

import android.graphics.drawable.Drawable;

class AppBarBackgroundAlphaTransitioner implements Transitioner {

    private static final int MAX_ALPHA = 255;

    private final Drawable appBarBackground;

    AppBarBackgroundAlphaTransitioner(Drawable appBarBackground) {
        this.appBarBackground = appBarBackground;
    }

    @Override
    public void onTransitionStateChange(float transitionState) {
        appBarBackground.setAlpha((int) (MAX_ALPHA - (MAX_ALPHA * transitionState)));
    }

}
