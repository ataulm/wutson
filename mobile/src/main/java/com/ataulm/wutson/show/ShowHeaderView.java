package com.ataulm.wutson.show;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.wutson.ClassContractBrokenException;
import com.ataulm.wutson.R;

public class ShowHeaderView extends LinearLayout {

    private TextView overviewTextView;

    public ShowHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.setOrientation(VERTICAL);
        View.inflate(getContext(), R.layout.merge_show_header_view, this);
        overviewTextView = (TextView) findViewById(R.id.show_header_text_overview);
    }

    @Override
    public void setOrientation(int orientation) {
        throw new ClassContractBrokenException();
    }

    public void display(Show show) {
        overviewTextView.setText(show.getOverview());
    }

}
