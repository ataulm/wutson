package com.ataulm.wutson.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;

public class AppBarWidget extends AppBarLayout {

    private Toolbar toolbar;

    public AppBarWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomAttributes(context, attrs);
    }

    private void applyCustomAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppBarWidget);
        int mergeLayoutId = typedArray.getResourceId(R.styleable.AppBarWidget_mergeLayoutId, R.layout.merge_app_bar_default);
        View.inflate(getContext(), mergeLayoutId, this);

        int backgroundColor = typedArray.getColor(R.styleable.AppBarWidget_appBarBackgroundColor, getResources().getColor(R.color.transparent));
        setBackgroundColor(backgroundColor);
        typedArray.recycle();

        setId(R.id.app_bar);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        toolbar = (Toolbar) findViewById(R.id.app_bar_tool_bar);
        if (toolbar == null) {
            throw DeveloperError.because("Custom app bar layouts must contain a Toolbar with id='@id/app_bar_tool_bar'");
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public static class ScrollingViewBehavior extends AppBarLayout.ScrollingViewBehavior {

        public ScrollingViewBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
            return dependency instanceof AppBarWidget;
        }

    }

}
