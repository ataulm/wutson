package com.ataulm.wutson.tmdb;

import com.google.gson.annotations.SerializedName;

import java.util.Iterator;
import java.util.List;

public class GsonGenres implements Iterable<GsonGenre> {

    @SerializedName("genres")
    public final List<GsonGenre> gsonGenres;

    private GsonGenres(List<GsonGenre> gsonGenres) {
        this.gsonGenres = gsonGenres;
    }

    @Override
    public Iterator<GsonGenre> iterator() {
        return gsonGenres.iterator();
    }

}
