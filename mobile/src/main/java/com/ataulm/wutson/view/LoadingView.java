package com.ataulm.wutson.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ataulm.wutson.R;

public class LoadingView extends FrameLayout {

    private View iconView;
    private TextView labelTextView;

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_loading, this);
        iconView = findViewById(R.id.loading_image_icon);
        labelTextView = (TextView) findViewById(R.id.loading_text_label);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int iconLeft = (int) ((getMeasuredWidth() * 0.5) - (iconView.getMeasuredWidth() * 0.5));
        int iconTop = (int) ((getMeasuredHeight() * 0.5) - (iconView.getMeasuredHeight() + labelTextView.getMeasuredHeight() * 0.5));
        int iconRight = iconLeft + iconView.getMeasuredWidth();
        int iconBottom = iconTop + iconView.getMeasuredHeight();
        iconView.layout(iconLeft, iconTop, iconRight, iconBottom);

        int labelLeft = (int) ((getMeasuredWidth() * 0.5) - (labelTextView.getMeasuredWidth() * 0.5));
        int labelTop = iconBottom;
        int labelRight = labelLeft + labelTextView.getMeasuredWidth();
        int labelBottom = labelTop + labelTextView.getMeasuredHeight();
        labelTextView.layout(labelLeft, labelTop, labelRight, labelBottom);
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == getVisibility()) {
            return;
        }
        super.setVisibility(visibility);
        updateTextAfterDelay();
    }

    private void updateTextAfterDelay() {
        postDelayed(updateTextRunnable, 150);
    }

    private final Runnable updateTextRunnable = new Runnable() {

        @Override
        public void run() {
            int step = labelTextView.getText().length();
            String text = textAtStepAfter(step);
            labelTextView.setText(text);

            if (getVisibility() == VISIBLE) {
                updateTextAfterDelay();
            }
        }

        private String textAtStepAfter(int step) {
            switch (step) {
                case 1:
                    return "oo";
                case 2:
                    return "ooo";
                case 3:
                    return "oooo";
                case 4:
                    return "ooooo";
                case 0:
                case 5:
                default:
                    return "o";
            }
        }

    };

}
