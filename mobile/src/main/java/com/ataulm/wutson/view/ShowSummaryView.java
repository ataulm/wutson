package com.ataulm.wutson.view;

import android.content.Context;
import android.support.annotation.MenuRes;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.bumptech.glide.Glide;

import java.net.URI;

public class ShowSummaryView extends FrameLayout {

    private static final float HEIGHT_BY_WIDTH_RATIO = 214f / 178;
    private static final float HALF_PIXEL = 0.5f;

    private ImageView posterImageView;
    private TextView titleTextView;
    private View overflowButtonView;
    private PopupMenu popupMenu;

    public ShowSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int desiredHeight = (int) (width * HEIGHT_BY_WIDTH_RATIO + HALF_PIXEL);

        int desiredHeightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, desiredHeightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_show_summary, this);

        posterImageView = (ImageView) findViewById(R.id.show_summary_image_poster);
        titleTextView = (TextView) findViewById(R.id.show_summary_text_title);
        overflowButtonView = findViewById(R.id.show_summary_button_overflow);
    }

    public void setPopupMenu(@MenuRes int menuResId, final OnMenuItemClickListener onMenuItemClickListener) {
        popupMenu = new PopupMenu(getContext(), overflowButtonView);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMenuItemClickListener.onClick(item);
                return true;
            }
        });
        popupMenu.inflate(menuResId);

        overflowButtonView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
        overflowButtonView.setVisibility(VISIBLE);
    }

    public void setPoster(URI uri) {
        posterImageView.setImageBitmap(null);

        Glide.with(getContext())
                .load(uri.toString())
                .into(posterImageView);
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public interface OnMenuItemClickListener {

        void onClick(MenuItem menuItem);

    }

}
