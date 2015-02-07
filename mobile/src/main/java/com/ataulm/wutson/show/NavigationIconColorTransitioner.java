package com.ataulm.wutson.show;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

class NavigationIconColorTransitioner implements Transitioner {

    private static final int TOTAL_FRACTION = 1;

    private static int COLOR_START = Color.WHITE;
    private static int COLOR_END = Color.BLACK;

    private final Drawable navigationIcon;
    private final ArgbEvaluator evaluator;

    NavigationIconColorTransitioner(Drawable navigationIcon, ArgbEvaluator evaluator) {
        this.navigationIcon = navigationIcon;
        this.evaluator = evaluator;
    }

    @Override
    public void onTransitionStateChange(float transitionState) {
        float fraction = TOTAL_FRACTION - transitionState;

        int newColor = (int) evaluator.evaluate(fraction, COLOR_START, COLOR_END);
        navigationIcon.setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
    }

}
