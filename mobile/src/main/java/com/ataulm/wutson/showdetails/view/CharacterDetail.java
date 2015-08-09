package com.ataulm.wutson.showdetails.view;

import com.ataulm.wutson.shows.Character;

class CharacterDetail implements Detail {

    private final com.ataulm.wutson.shows.Character character;

    CharacterDetail(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    @Override
    public Type getType() {
        return Type.CHARACTER;
    }

}
