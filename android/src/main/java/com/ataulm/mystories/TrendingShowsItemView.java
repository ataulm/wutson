package com.ataulm.mystories;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

class TrendingShowsItemView extends FrameLayout {

    private ImageView imageView;

    public TrendingShowsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrendingShowsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_trending_shows_list_standard, this);
        imageView = (ImageView) findViewById(R.id.trending_shows_item_poster);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int contentWidth = imageView.getDrawable().getIntrinsicWidth();
        int contentHeight = imageView.getDrawable().getIntrinsicHeight();
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);

        int desiredHeight = (int) (1f * (viewWidth * contentHeight) / contentWidth);
        int desiredHeightSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, desiredHeightSpec);
    }

}
