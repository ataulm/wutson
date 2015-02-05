package com.ataulm.wutson.show;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.Presenter;
import com.ataulm.wutson.Presenters;
import com.ataulm.wutson.R;
import com.ataulm.wutson.ToastDisplayer;
import com.ataulm.wutson.WutsonApplication;
import com.bumptech.glide.Glide;

public class ShowView extends FrameLayout implements Presenter<Show> {

    private ImageView posterImageView;
    private TextView seasonsTextView;
    private Presenter<Show> headerPresenter;
    private Presenter<Cast> castPresenter;

    public ShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_show, this);
        posterImageView = (ImageView) findViewById(R.id.show_view_image_poster);
        seasonsTextView = (TextView) findViewById(R.id.show_view_text_seasons);
        headerPresenter = Presenters.findById(this, R.id.show_header);
        castPresenter = Presenters.findById(this, R.id.show_view_cast);
    }

    @Override
    public void present(Show show) {
        Glide.with(getContext()).load(show.getPosterUri().toString()).into(posterImageView);
        headerPresenter.present(show);
        seasonsTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastDisplayer toastDisplayer = ((WutsonApplication) getContext().getApplicationContext()).getToastDisplayer();
                toastDisplayer.display("show me the seasons!");
            }
        });
        castPresenter.present(show.getCast());
    }

}
