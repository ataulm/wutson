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

import java.net.URI;

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

    void setBackdrop(URI backdropUri) {
        Glide.with(getContext())
                .load(backdropUri.toString())
                .into(backdropImageView);
    }

    void setOverview(String overview) {
        showHeaderView.setText(overview);
    }

    void setCast(Cast cast) {
        castView.display(cast);
    }

}
