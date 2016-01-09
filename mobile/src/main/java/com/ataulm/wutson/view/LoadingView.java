package com.ataulm.wutson.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;

public class LoadingView extends LinearLayout {

    private TextView labelTextView;

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        super.setOrientation(VERTICAL);
        View.inflate(getContext(), R.layout.merge_loading, this);
        labelTextView = (TextView) findViewById(R.id.loading_text_label);

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

    @Override
    public final void setOrientation(int orientation) {
        throw DeveloperError.methodCannotBeCalledOutsideThisClass();
    }

}
