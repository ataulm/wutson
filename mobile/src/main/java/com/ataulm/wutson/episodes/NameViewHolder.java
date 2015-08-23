package com.ataulm.wutson.episodes;

import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ataulm.wutson.R;

public final class NameViewHolder extends DetailViewHolder {

    @ColorInt
    int accentColor;

    public static NameViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent, @ColorInt int accentColor) {
        View view = layoutInflater.inflate(R.layout.view_episode_details_item_name, parent, false);
        return new NameViewHolder(view, accentColor);
    }

    private NameViewHolder(View itemView, @ColorInt int accentColor) {
        super(itemView);
        this.accentColor = accentColor;
    }

    @Override
    public void bind(Detail detail) {
        String name = ((NameDetail) detail).getName();
        ((TextView) itemView).setText(name);
        itemView.setBackgroundColor(accentColor);
    }

}
