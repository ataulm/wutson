package com.ataulm.wutson.showdetails.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ataulm.wutson.R;

public final class CharacterDetailViewHolder extends DetailViewHolder {

    public static CharacterDetailViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.view_show_details_item_character, parent, false);
        return new CharacterDetailViewHolder(view);
    }

    private CharacterDetailViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Detail detail) {
        // TODO: bind character view
        ((TextView) itemView).setText(((CharacterDetail) detail).getCharacter().getName());
    }

}
