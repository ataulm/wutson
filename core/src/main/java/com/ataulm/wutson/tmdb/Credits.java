package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

import java.lang.*;
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
