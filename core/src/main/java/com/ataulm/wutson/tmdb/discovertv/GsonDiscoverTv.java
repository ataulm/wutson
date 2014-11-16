package com.ataulm.wutson.tmdb.discovertv;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class GsonDiscoverTv {

    @SerializedName("page")
    int data;

    @SerializedName("results")
    List<Show> results;

    @SerializedName("totalPages")
    int totalPages;

    @SerializedName("totalResults")
    int totalResults;

    static class Show {

        @SerializedName("id")
        int id;

        @SerializedName("name")
        String name;

        @SerializedName("poster_path")
        String posterPath;

        @SerializedName("first_air_date")
        String firstAirDate;

        @SerializedName("popularity")
        double popularity;

        @SerializedName("vote_average")
        double voteAverage;

        @SerializedName("vote_count")
        int voteCount;

    }

}
