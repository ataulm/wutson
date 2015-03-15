package com.ataulm.wutson.tmdb.gson;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Iterator;

public class GsonDiscoverTv implements Iterable<GsonDiscoverTv.Shows.Show> {

    @SerializedName("results")
    public final Shows shows;

    private GsonDiscoverTv(Shows shows) {
        this.shows = shows;
    }

    @Override
    public Iterator<Shows.Show> iterator() {
        return shows.iterator();
    }

    public static class Shows extends ArrayList<Shows.Show> {

        public static class Show {

            @SerializedName("id")
            public final String id;

            @SerializedName("name")
            public final String name;

            @SerializedName("poster_path")
            public final String posterPath;

            @SerializedName("backdrop_path")
            public final String backdropPath;

            private Show(String id, String name, String posterPath, String backdropPath) {
                this.id = id;
                this.name = name;
                this.posterPath = posterPath;
                this.backdropPath = backdropPath;
            }

        }

    }


}
