package com.ataulm.wutson.myshows;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;

public class TrackedShowSummaryView extends LinearLayout {

    private TextView showNameTextView;

    public TrackedShowSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.setOrientation(VERTICAL);
        View.inflate(getContext(), R.layout.merge_tracked_show_summary, this);
        showNameTextView = (TextView) findViewById(R.id.tracked_show_summary_text_show_name);
    }

    @Override
    public final void setOrientation(int orientation) {
        throw DeveloperError.methodCannotBeCalledOutsideThisClass();
    }

    void setShowName(String showName) {
        showNameTextView.setText(showName);
    }

}
