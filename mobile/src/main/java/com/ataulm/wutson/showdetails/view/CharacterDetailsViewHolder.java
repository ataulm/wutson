package com.ataulm.wutson.showdetails.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;

public final class CharacterDetailsViewHolder extends DetailsViewHolder {

    public static CharacterDetailsViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.view_show_details_item_character, parent, false);
        return new CharacterDetailsViewHolder(view);
    }

    private CharacterDetailsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Detail detail) {
        // TODO: bind character view
        ((CharacterDetail) detail).getCharacter();
    }

}
