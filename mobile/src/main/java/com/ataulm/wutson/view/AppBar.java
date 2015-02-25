package com.ataulm.wutson.view;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.ataulm.wutson.R;

public class AppBar extends Toolbar {

    private TextView titleTextView;

    public AppBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public AppBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setId(R.id.app_bar);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_app_bar, this);
        titleTextView = (TextView) findViewById(R.id.app_bar_text_title);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredHeight = getResources().getDimensionPixelSize(R.dimen.app_bar_height);
        int desiredHeightSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, desiredHeightSpec);
    }

    @Override
    public void setTitle(int resId) {
        titleTextView.setText(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        titleTextView.setText(title);
    }

}
