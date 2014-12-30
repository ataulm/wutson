package com.ataulm.wutson.popularshows;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

class PopularShowsListItemView extends FrameLayout {

    private static final double POSTER_RATIO = 1.5;

    public PopularShowsListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopularShowsListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int desiredHeight = (int) (width * POSTER_RATIO);
        int desiredHeightSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, desiredHeightSpec);
    }

}
