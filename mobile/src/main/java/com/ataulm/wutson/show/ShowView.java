package com.ataulm.wutson.show;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;
import com.ataulm.wutson.ToastDisplayer;
import com.bumptech.glide.Glide;

public class ShowView extends FrameLayout {

    private View contentView;
    private ImageView posterImageView;
    private TextView seasonsTextView;
    private ShowHeaderView showHeaderView;
    private CastView castView;
    private ScrollListener listener;

    public ShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_show, this);
        contentView = findViewById(R.id.show_view_scroll_content);
        contentView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                if (listener == null) {
                    return;
                }
                listener.onShowViewScrolled(contentView.getScrollY());
            }

        });

        posterImageView = (ImageView) findViewById(R.id.show_view_image_poster);
        seasonsTextView = (TextView) findViewById(R.id.show_view_text_seasons);
        showHeaderView = (ShowHeaderView) findViewById(R.id.show_header);
        castView = (CastView) findViewById(R.id.show_view_cast);
    }

    void setScrollListener(ScrollListener listener) {
        this.listener = listener;
    }

    public void display(Show show) {
        Glide.with(getContext())
                .load(show.getPosterUri().toString())
                .into(posterImageView);

        showHeaderView.display(show);
        castView.display(show.getCast());
        seasonsTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ToastDisplayer toastDisplayer = Jabber.toastDisplayer();
                toastDisplayer.display("show me the seasons!");
            }

        });

        contentView.setVisibility(VISIBLE);
    }

    interface ScrollListener {

        void onShowViewScrolled(int scrollY);

    }

}
