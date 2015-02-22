package com.ataulm.wutson.show;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ataulm.wutson.R;
import com.bumptech.glide.Glide;

public class ShowOverviewView extends FrameLayout {

    private ImageView posterImageView;
    private ShowHeaderView showHeaderView;
    private CastView castView;

    public ShowOverviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_show_overview, this);
        posterImageView = (ImageView) findViewById(R.id.show_view_image_poster);
        showHeaderView = (ShowHeaderView) findViewById(R.id.show_header);
        castView = (CastView) findViewById(R.id.show_view_cast);
    }

    public void display(Show show) {
        Glide.with(getContext())
                .load(show.getPosterUri().toString())
                .into(posterImageView);

        showHeaderView.display(show);
        castView.display(show.getCast());
    }

}
