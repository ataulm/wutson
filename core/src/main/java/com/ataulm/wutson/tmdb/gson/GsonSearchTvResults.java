package com.ataulm.wutson.tmdb.gson;

import com.google.gson.annotations.SerializedName;

import java.util.Iterator;
import java.util.List;

public final class GsonSearchTvResults implements Iterable<GsonSearchTvResult> {

    @SerializedName("results")
    final List<GsonSearchTvResult> results;

    private GsonSearchTvResults(List<GsonSearchTvResult> results) {
        this.results = results;
    }

    @Override
    public Iterator<GsonSearchTvResult> iterator() {
        return results.iterator();
    }

}
