package com.ataulm.wutson.view;

import android.content.Context;
import android.util.AttributeSet;

public class WidthBasedCorrectAspectRatioImageView extends CrossfadeImageView {

    public WidthBasedCorrectAspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getDrawable() == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int drawableWidth = getDrawable().getIntrinsicWidth();
        int drawableHeight = getDrawable().getIntrinsicHeight();
        double ratio = 1f * drawableHeight / drawableWidth;

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int desiredHeight = (int) (width * ratio + 0.5f);
        int desiredHeightSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, desiredHeightSpec);
    }

}
