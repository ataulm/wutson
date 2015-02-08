package com.ataulm.wutson.model;

import com.google.gson.annotations.SerializedName;

import java.util.Iterator;
import java.util.List;

public class Credits implements Iterable<Character> {

    @SerializedName("cast")
    List<Character> characters;

    @Override
    public Iterator<Character> iterator() {
        return characters.iterator();
    }

}
