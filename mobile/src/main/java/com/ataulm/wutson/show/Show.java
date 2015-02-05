package com.ataulm.wutson.show;

import java.net.URI;

public class Show {

    private final String name;
    private final String overview;
    private final URI posterUri;
    private final Cast cast;

    Show(String name, String overview, URI posterUri, Cast cast) {
        this.name = name;
        this.overview = overview;
        this.posterUri = posterUri;
        this.cast = cast;
    }

    public String getName() {
        return name;
    }

    public URI getPosterUri() {
        return posterUri;
    }

    public String getOverview() {
        return overview;
    }

    public Cast getCast() {
        return cast;
    }

}
