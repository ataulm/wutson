package com.ataulm.wutson.discover;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.bumptech.glide.Glide;

public class ShowsInGenreItemView extends FrameLayout {

    private static final float HEIGHT_BY_WIDTH_RATIO = 214f / 178;
    private static final float HALF_PIXEL = 0.5f;

    private ImageView posterImageView;
    private TextView nameTextView;

    public ShowsInGenreItemView(Context context, AttributeSet attrs) {
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
        View.inflate(getContext(), R.layout.merge_discover_by_genre_item, this);

        posterImageView = (ImageView) findViewById(R.id.discover_by_genre_image_poster);
        nameTextView = (TextView) findViewById(R.id.discover_by_genre_text_name);
    }

    public void display(final Show show) {
        posterImageView.setImageBitmap(null);
        nameTextView.setVisibility(INVISIBLE);

        Glide.with(getContext())
                .load(show.getPosterUri().toString())
                .asBitmap()
                .into(posterImageView);
        nameTextView.setText(show.getName());
        nameTextView.setVisibility(VISIBLE);
    }

}
