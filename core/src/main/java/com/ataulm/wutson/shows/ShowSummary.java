package com.ataulm.wutson.shows;

import java.net.URI;

public class ShowSummary {

    private final ShowId id;
    private final String name;
    private final URI posterUri;
    private final URI backdropUri;

    public ShowSummary(ShowId id, String name, URI posterUri, URI backdropUri) {
        this.id = id;
        this.name = name;
        this.posterUri = posterUri;
        this.backdropUri = backdropUri;
    }

    public ShowId getId() {
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
