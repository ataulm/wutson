package com.ataulm.watson;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

class TrendingShowsLayoutManager extends GridLayoutManager {

    private static final int COLUMN_COUNT = 2;

    public static TrendingShowsLayoutManager newInstance(Context context) {
        TrendingShowsLayoutManager layoutManager = new TrendingShowsLayoutManager(context, COLUMN_COUNT);
        return layoutManager;
    }

    private TrendingShowsLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

}
