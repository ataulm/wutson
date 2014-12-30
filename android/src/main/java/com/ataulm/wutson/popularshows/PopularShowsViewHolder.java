package com.ataulm.wutson.popularshows;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.novoda.pxfetcher.PixelFetchers;

class PopularShowsViewHolder extends RecyclerView.ViewHolder {

    private final ImageView posterImageView;
    private final TextView showTextView;
    private final TextView voteAverageTextView;

    public PopularShowsViewHolder(View itemView) {
        super(itemView);
        posterImageView = (ImageView) itemView.findViewById(R.id.popular_shows_image_poster);
        showTextView = (TextView) itemView.findViewById(R.id.popular_shows_text_show);
        voteAverageTextView = (TextView) itemView.findViewById(R.id.popular_shows_vote_average_name);
    }

    void bind(PopularShow show) {
        itemView.setContentDescription(show.getShowName());

        showTextView.setText(show.getShowName());
        voteAverageTextView.setText(show.getVoteAverage());

        Context context = itemView.getContext();
        PixelFetchers.getInstance(context).load("http://i0.kym-cdn.com/photos/images/newsfeed/000/173/576/Wat8.jpg", posterImageView);
    }

}
