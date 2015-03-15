package com.ataulm.wutson.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.bumptech.glide.Glide;

import java.net.URI;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

// not a problem - https://code.google.com/p/android/issues/detail?id=67434
@SuppressLint("Instantiatable")
class ShowOverviewView extends FrameLayout {

    private final LayoutInflater layoutInflater;

    private ImageView backdropImageView;
    private TextView showHeaderView;
    private View castView;
    private ViewGroup castMembersContainer;

    public ShowOverviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_show_overview, this);
        backdropImageView = (ImageView) findViewById(R.id.show_view_image_backdrop);
        showHeaderView = (TextView) findViewById(R.id.show_header_text_overview);
        castView = findViewById(R.id.show_view_cast);
        castMembersContainer = (ViewGroup) findViewById(R.id.cast_container);
    }

    void setBackdrop(URI backdropUri) {
        Glide.with(getContext())
                .load(backdropUri.toString())
                .into(backdropImageView);
    }

    void setOverview(String overview) {
        showHeaderView.setText(overview);
    }

    void setCast(Cast cast) {
        castMembersContainer.removeAllViews();

        if (cast.isEmpty()) {
            castView.setVisibility(GONE);
            return;
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        Resources resources = getResources();
        layoutParams.setMargins(
                resources.getDimensionPixelSize(R.dimen.character_margin_left),
                resources.getDimensionPixelSize(R.dimen.character_margin_top),
                resources.getDimensionPixelSize(R.dimen.character_margin_right),
                resources.getDimensionPixelSize(R.dimen.character_margin_bottom));

        castView.setVisibility(VISIBLE);

        for (Character character : cast) {
            View characterView = getViewFor(character);
            castMembersContainer.addView(characterView, layoutParams);
        }
    }

    private View getViewFor(Character character) {
        CharacterView characterView = ((CharacterView) layoutInflater.inflate(R.layout.view_character, this, false));
        characterView.display(character);
        return characterView;
    }

}
