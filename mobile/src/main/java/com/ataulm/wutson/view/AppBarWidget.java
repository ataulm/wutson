package com.ataulm.wutson.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;

public class AppBarWidget extends AppBarLayout {

    private Toolbar toolbar;

    public AppBarWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomAttributes(context, attrs);

        resetElevationToZero();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void resetElevationToZero() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        setElevation(0);
    }

    private void applyCustomAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppBarWidget);
        int mergeLayoutId = typedArray.getResourceId(R.styleable.AppBarWidget_mergeLayoutId, R.layout.merge_app_bar_default);
        View.inflate(getContext(), mergeLayoutId, this);

        Drawable drawable = typedArray.getDrawable(R.styleable.AppBarWidget_appBarBackground);
        if (drawable != null) {
            setBackground(drawable);
        } else {
            int backgroundResource = typedArray.getColor(R.styleable.AppBarWidget_appBarBackground, getResources().getColor(R.color.transparent));
            setBackgroundColor(backgroundResource);
        }

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
