package com.ataulm.mystories;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

class TrendingShowsItemView extends ImageView {

    public TrendingShowsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrendingShowsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // TODO: do NOT allow this to know about the model. Only expose setters for the components. The ViewHolder will bind.

}
