package com.ataulm.wutson;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TrendingShowsItemView extends FrameLayout {

    private TextView titleView;

    public TrendingShowsItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrendingShowsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_trending_shows_list_standard, this);

        titleView = (TextView) findViewById(R.id.trending_shows_item_title);
    }

    public void update(Show show) {
        titleView.setText(show.getName());
    }

}
