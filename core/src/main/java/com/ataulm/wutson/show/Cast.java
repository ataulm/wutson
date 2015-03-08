package com.ataulm.wutson.show;

import java.util.Iterator;
import java.util.List;

public class Cast implements Iterable<Character> {

    private final List<Character> characters;

    public Cast(List<Character> characters) {
        this.characters = characters;
    }

    int size() {
        return characters.size();
    }

    @Override
    public Iterator<Character> iterator() {
        return characters.iterator();
    }

}
