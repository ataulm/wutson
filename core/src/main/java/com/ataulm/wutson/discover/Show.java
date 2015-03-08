package com.ataulm.wutson.discover;

import java.net.URI;

public class Show {

    private final String id;
    private final String name;
    private final URI posterUri;

    Show(String id, String name, URI posterUri) {
        this.id = id;
        this.name = name;
        this.posterUri = posterUri;
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    URI getPosterUri() {
        return posterUri;
    }

}
