package com.ataulm.wutson;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class TrendingShowsItemView extends FrameLayout {

    public TrendingShowsItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrendingShowsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO: dis fudged up UX. Y U NO LISTEN? - Romain Guy, probably
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_trending_shows_list_standard, this);
    }

}
