package com.ataulm.wutson.core.showdetails;

import java.util.Iterator;
import java.util.List;

public class Cast implements Iterable<Character> {

    private final List<Character> characters;

    public Cast(List<Character> characters) {
        this.characters = characters;
    }

    public boolean isEmpty() {
        return characters.isEmpty();
    }

    @Override
    public Iterator<Character> iterator() {
        return characters.iterator();
    }

}
