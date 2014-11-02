package com.ataulm.mystories;

import com.ataulm.mystories.tmdb.discovertv.DiscoverTv;

public class Show {

    private final Id id;
    private final Name name;
    private final PosterPath posterPath;

    public static Show from(DiscoverTv.Show discoverTvShow) {
        Show.Id id = new Show.Id(discoverTvShow.getId());
        Show.Name name = new Show.Name(discoverTvShow.getName());
        Show.PosterPath posterPath = new Show.PosterPath(discoverTvShow.getPosterPath());

        return new Show(id, name, posterPath);
    }

    public Show(Id id, Name name, PosterPath posterPath) {
        this.id = id;
        this.name = name;
        this.posterPath = posterPath;
    }

    public String getName() {
        return name.toString();
    }

    public String getPosterPathAsUrl() {
        return posterPath.asUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Show show = (Show) o;

        return id.equals(show.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return name.toString();
    }

    static class Id {

        private final int id;

        Id(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Id id1 = (Id) o;

            return id == id1.id;
        }

        @Override
        public int hashCode() {
            return id;
        }

    }

    static class Name {

        private final String name;

        Name(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

    }

    static class PosterPath {

        private final String url;

        PosterPath(String url) {
            this.url = url;
        }

        public String asUrl() {
            return url;
        }

    }

}
