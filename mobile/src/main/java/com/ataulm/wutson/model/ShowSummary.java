package com.ataulm.wutson.model;

import java.net.URI;

public class ShowSummary {

    private final String id;
    private final String name;
    private final URI posterUri;
    private final URI backdropUri;

    public ShowSummary(String id, String name, URI posterUri, URI backdropUri) {
        this.id = id;
        this.name = name;
        this.posterUri = posterUri;
        this.backdropUri = backdropUri;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public URI getPosterUri() {
        return posterUri;
    }

    public URI getBackdropUri() {
        return backdropUri;
    }

}
