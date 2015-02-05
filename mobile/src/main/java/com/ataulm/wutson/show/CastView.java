package com.ataulm.wutson.show;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ataulm.wutson.Presenter;
import com.ataulm.wutson.Presenters;
import com.ataulm.wutson.R;

public class CastView extends LinearLayout implements Presenter<Cast> {

    private final LayoutInflater layoutInflater;

    public CastView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CastView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void setOrientation(int orientation) {
        throw new IllegalStateException("cast view is fixed as vertical");
    }

    @Override
    protected void onFinishInflate() {
        super.setOrientation(VERTICAL);
    }

    @Override
    public void present(Cast cast) {
        removeAllViews();
        for (Character character : cast) {
            View characterView = getViewFor(character);
            addView(characterView);
        }
    }

    private View getViewFor(Character character) {
        Presenter<Character> characterPresenter = Presenters.inflateFromLayout(layoutInflater, this, R.layout.view_character);
        characterPresenter.present(character);

        return (View) characterPresenter;
    }

}
