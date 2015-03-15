package com.ataulm.wutson.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.bumptech.glide.Glide;

// not a problem - https://code.google.com/p/android/issues/detail?id=67434
@SuppressLint("Instantiatable")
class ShowOverviewView extends FrameLayout {

    private ImageView backdropImageView;
    private TextView showHeaderView;
    private CastView castView;

    public ShowOverviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_show_overview, this);
        backdropImageView = (ImageView) findViewById(R.id.show_view_image_backdrop);
        showHeaderView = (TextView) findViewById(R.id.show_header_text_overview);
        castView = (CastView) findViewById(R.id.show_view_cast);
    }

    public void display(Show show) {
        Glide.with(getContext())
                .load(show.getBackdropUri().toString())
                .into(backdropImageView);

        showHeaderView.setText(show.getOverview());
        castView.display(show.getCast());
    }

}
