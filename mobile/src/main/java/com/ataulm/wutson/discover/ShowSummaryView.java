package com.ataulm.wutson.discover;

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
class ShowSummaryView extends FrameLayout {

    private static final float HEIGHT_BY_WIDTH_RATIO = 214f / 178;
    private static final float HALF_PIXEL = 0.5f;

    private ImageView posterImageView;
    private TextView titleTextView;

    public ShowSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int desiredHeight = (int) (width * HEIGHT_BY_WIDTH_RATIO + HALF_PIXEL);

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_discover_show_summary, this);

        posterImageView = (ImageView) findViewById(R.id.discover_show_summary_image_poster);
        titleTextView = (TextView) findViewById(R.id.discover_show_summary_text_title);
    }

    void setPoster(String uri) {
        posterImageView.setImageBitmap(null);

        Glide.with(getContext())
                .load(uri)
                .asBitmap()
                .into(posterImageView);
    }

    void setTitle(String title) {
        titleTextView.setText(title);
    }

}
