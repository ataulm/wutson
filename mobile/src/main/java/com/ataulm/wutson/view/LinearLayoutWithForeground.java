package com.ataulm.wutson.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class LinearLayoutWithForeground extends LinearLayout {

    private static final int[] EXPECTED_ATTRS = {android.R.attr.foreground};
    private static final int INDEX_ANDROID_FOREGROUND = 0;

    private static final int INVALID_FOREGROUND_ID = 0;

    private Drawable foreground;

    public LinearLayoutWithForeground(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, EXPECTED_ATTRS);
        try {
            int resourceId = a.getResourceId(INDEX_ANDROID_FOREGROUND, INVALID_FOREGROUND_ID);
            if (resourceId == INVALID_FOREGROUND_ID) {
                return;
            }
            Drawable drawable = DrawableFactory.getDrawableFrom(getResources(), resourceId);
            updateForeground(drawable);
        } finally {
            a.recycle();
        }
    }

    public Drawable getForegroundCompat() {
        return foreground;
    }

    public void setForegroundCompat(Drawable foreground) {
        if (this.foreground == foreground) {
            return;
        }
        removeOldForegroundIfAny();
        updateForeground(foreground);
    }

    private void updateForeground(Drawable foreground) {
        this.foreground = foreground;
        if (foreground == null) {
            setWillNotDraw(true);
        } else {
            setWillNotDraw(false);
            foreground.setCallback(this);
            if (foreground.isStateful()) {
                foreground.setState(getDrawableState());
            }
        }
        requestLayout();
        invalidate();
    }

    private void removeOldForegroundIfAny() {
        if (foreground != null) {
            foreground.setCallback(null);
            unscheduleDrawable(foreground);
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || (foreground.equals(who));
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (foreground != null) {
            foreground.jumpToCurrentState();
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (foreground != null && foreground.isStateful()) {
            foreground.setState(getDrawableState());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (foreground != null) {
            foreground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (foreground != null) {
            foreground.draw(canvas);
        }
    }

}
