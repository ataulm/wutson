package com.ataulm.wutson.show;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.wutson.Displayer;
import com.ataulm.wutson.R;

public class ShowHeaderView extends LinearLayout implements Displayer<Show> {

    private TextView nameTextView;
    private TextView overviewTextView;

    public ShowHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_show_header_view, this);
        nameTextView = (TextView) findViewById(R.id.show_header_text_name);
        overviewTextView = (TextView) findViewById(R.id.show_header_text_overview);
    }

    @Override
    public void display(Show show) {
        nameTextView.setText(show.getName());
        overviewTextView.setText(show.getOverview());
    }

}
