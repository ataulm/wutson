package com.ataulm.wutson.discover;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.view.Displayer;
import com.ataulm.wutson.R;
import com.bumptech.glide.Glide;

public class ShowsInGenreItemView extends FrameLayout implements Displayer<Show> {

    private final static float HEIGHT_BY_WIDTH_RATIO = 272f / 185;
    private final static float HALF_PIXEL = 0.5f;

    private ImageView posterImageView;
    private TextView nameTextView;

    public ShowsInGenreItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowsInGenreItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int desiredHeight = (int) (width * HEIGHT_BY_WIDTH_RATIO + HALF_PIXEL);


        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_shows_in_genre_item, this);

        posterImageView = (ImageView) findViewById(R.id.shows_in_genre_image_poster);
        nameTextView = (TextView) findViewById(R.id.shows_in_genre_text_name);
    }

    @Override
    public void display(Show show) {
        Glide.with(getContext()).load(show.getPosterUri().toString()).into(posterImageView);
        nameTextView.setText(show.getName());
    }

}
