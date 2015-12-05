package com.ataulm.wutson.showdetails.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.bumptech.glide.Glide;

public final class CharacterDetailViewHolder extends DetailViewHolder {

    private final ImageView headshotImageView;
    private final TextView actorNameTextView;
    private final TextView characterNameTextView;

    public static CharacterDetailViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.view_show_details_item_character, parent, false);
        ImageView headshotImageView = (ImageView) view.findViewById(R.id.show_details_item_character_image_headshot);
        TextView characterNameTextView = (TextView) view.findViewById(R.id.show_details_item_character_text_character);
        TextView actorNameTextView = (TextView) view.findViewById(R.id.show_details_item_character_text_actor);
        return new CharacterDetailViewHolder(view, headshotImageView, actorNameTextView, characterNameTextView);
    }

    private CharacterDetailViewHolder(View itemView, ImageView headshotImageView, TextView actorNameTextView, TextView characterNameTextView) {
        super(itemView);
        this.headshotImageView = headshotImageView;
        this.actorNameTextView = actorNameTextView;
        this.characterNameTextView = characterNameTextView;
    }

    @Override
    public void bind(Detail detail) {
        CharacterDetail characterDetail = (CharacterDetail) detail;
        Glide.with(itemView.getContext())
                .load(characterDetail.getCharacter().getActor().getProfileUri().toString())
                .centerCrop()
                .into(headshotImageView);

        characterNameTextView.setText(characterDetail.getCharacter().getName());
        actorNameTextView.setText(characterDetail.getCharacter().getActor().getName());
    }

}
