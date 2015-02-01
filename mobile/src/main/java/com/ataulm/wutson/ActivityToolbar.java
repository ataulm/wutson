package com.ataulm.wutson;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

public class ActivityToolbar extends Toolbar {

    public ActivityToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActivityToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredHeight = getResources().getDimensionPixelSize(R.dimen.toolbar_height);
        int desiredHeightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, desiredHeightMeasureSpec);
    }

}
