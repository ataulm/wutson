package com.ataulm.wutson.shows.myshows;

import com.ataulm.wutson.shows.ShowId;

import java.net.URI;

public class SearchTvResult {

    private final ShowId id;
    private final String name;
    private final String overview;
    private final URI posterUri;
    private final URI backdropUri;

    public SearchTvResult(ShowId id, String name, String overview, URI posterUri, URI backdropUri) {
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.posterUri = posterUri;
        this.backdropUri = backdropUri;
    }

    public ShowId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public URI getPosterUri() {
        return posterUri;
    }

    public URI getBackdropUri() {
        return backdropUri;
    }

}
