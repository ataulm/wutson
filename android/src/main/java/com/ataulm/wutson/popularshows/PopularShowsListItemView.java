package com.ataulm.wutson.popularshows;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.bumptech.glide.Glide;

class PopularShowsListItemView extends FrameLayout {

    private static final double POSTER_RATIO = 1.5;

    private ImageView posterImageView;
    private TextView showTextView;
    private TextView voteAverageTextView;

    public PopularShowsListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopularShowsListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_popular_shows_list_item, this);

        posterImageView = (ImageView) findViewById(R.id.popular_shows_image_poster);
        showTextView = (TextView) findViewById(R.id.popular_shows_text_show_name);
        voteAverageTextView = (TextView) findViewById(R.id.popular_shows_vote_average);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int desiredHeight = (int) (width * POSTER_RATIO);
        int desiredHeightSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, desiredHeightSpec);
    }

    public void update(PopularShow show) {
        setContentDescription(show.getShowName());

        showTextView.setText(show.getShowName());
        voteAverageTextView.setText(show.getVoteAverage());

        Glide.with(posterImageView.getContext()).load(show.getPosterUrl()).into(posterImageView);
    }

}
