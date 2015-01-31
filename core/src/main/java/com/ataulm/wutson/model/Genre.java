package com.ataulm.wutson.model;

import com.google.gson.annotations.SerializedName;

public class Genre {

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Genre: " + name + " (" + id + ")";
    }

}
