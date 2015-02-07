package com.ataulm.wutson.show;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.Displayer;
import com.ataulm.wutson.Displayers;
import com.ataulm.wutson.R;
import com.ataulm.wutson.ToastDisplayer;
import com.ataulm.wutson.WutsonApplication;
import com.bumptech.glide.Glide;

public class ShowView extends FrameLayout implements Displayer<Show> {

    private ImageView posterImageView;
    private TextView seasonsTextView;
    private Displayer<Show> headerDisplayer;
    private Displayer<Cast> castDisplayer;
    private ScrollListener listener;

    public ShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_show, this);
        final View scrollView = findViewById(R.id.show_scroll);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                if (listener == null) {
                    return;
                }
                listener.onShowViewScrolled(scrollView.getScrollY());
            }

        });

        posterImageView = (ImageView) findViewById(R.id.show_view_image_poster);
        seasonsTextView = (TextView) findViewById(R.id.show_view_text_seasons);
        headerDisplayer = Displayers.findById(this, R.id.show_header);
        castDisplayer = Displayers.findById(this, R.id.show_view_cast);
    }

    void setScrollListener(ScrollListener listener) {
        this.listener = listener;
    }

    @Override
    public void display(Show show) {
        Glide.with(getContext())
                .load(show.getPosterUri().toString())
                .into(posterImageView);

        headerDisplayer.display(show);
        castDisplayer.display(show.getCast());
        seasonsTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ToastDisplayer toastDisplayer = ((WutsonApplication) getContext().getApplicationContext()).getToastDisplayer();
                toastDisplayer.display("show me the seasons!");
            }

        });
    }

    interface ScrollListener {

        void onShowViewScrolled(int scrollY);

    }

}
