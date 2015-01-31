package com.ataulm.wutson.model;

import com.google.gson.annotations.SerializedName;

import java.util.Iterator;
import java.util.List;

public class Genres implements Iterable<Genre> {

    @SerializedName("genres")
    List<Genre> genres;

    public int size() {
        return genres.size();
    }

    @Override
    public Iterator<Genre> iterator() {
        return genres.iterator();
    }

}
