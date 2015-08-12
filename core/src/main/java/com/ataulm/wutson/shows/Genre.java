package com.ataulm.wutson.shows;

public class Genre implements Comparable<Genre> {

    private final String id;
    private final String name;

    public Genre(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Genre another) {
        return name.compareTo(another.name);
    }

}
