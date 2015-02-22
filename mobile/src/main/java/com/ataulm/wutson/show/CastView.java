package com.ataulm.wutson.show;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ataulm.wutson.R;

public class CastView extends LinearLayout {

    private final LayoutInflater layoutInflater;

    private ViewGroup castContainer;

    public CastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void setOrientation(int orientation) {
        throw new IllegalStateException("cast view is fixed as vertical");
    }

    @Override
    protected void onFinishInflate() {
        super.setOrientation(VERTICAL);
        View.inflate(getContext(), R.layout.merge_cast, this);
        castContainer = (ViewGroup) findViewById(R.id.cast_container);
    }

    public void display(Cast cast) {
        if (cast.size() == 0) {
            return;
        }

        CastView.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Resources resources = getResources();
        layoutParams.setMargins(
                resources.getDimensionPixelSize(R.dimen.character_margin_left),
                resources.getDimensionPixelSize(R.dimen.character_margin_top),
                resources.getDimensionPixelSize(R.dimen.character_margin_right),
                resources.getDimensionPixelSize(R.dimen.character_margin_bottom));

        setVisibility(VISIBLE);
        for (Character character : cast) {
            View characterView = getViewFor(character);
            castContainer.addView(characterView, layoutParams);
        }
    }

    private View getViewFor(Character character) {
        CharacterView characterView = ((CharacterView) layoutInflater.inflate(R.layout.view_character, this, false));
        characterView.display(character);
        return characterView;
    }

}
