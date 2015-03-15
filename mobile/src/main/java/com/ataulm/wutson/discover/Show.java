package com.ataulm.wutson.discover;

import java.net.URI;

public class Show {

    private final String id;
    private final String name;
    private final URI posterUri;
    private final URI backdropUri;

    Show(String id, String name, URI posterUri, URI backdropUri) {
        this.id = id;
        this.name = name;
        this.posterUri = posterUri;
        this.backdropUri = backdropUri;
    }

    public String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    URI getPosterUri() {
        return posterUri;
    }

    URI getBackdropUri() {
        return backdropUri;
    }

}
