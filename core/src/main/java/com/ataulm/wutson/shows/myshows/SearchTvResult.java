package com.ataulm.wutson.shows.myshows;

import com.ataulm.wutson.Optional;
import com.ataulm.wutson.shows.ShowId;

import java.net.URI;

public class SearchTvResult {

    private final ShowId id;
    private final String name;
    private final Optional<String> overview;
    private final Optional<URI> posterUri;

    public SearchTvResult(ShowId id, String name, Optional<String> overview, Optional<URI> posterUri) {
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.posterUri = posterUri;
    }

    public ShowId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getOverview() {
        return overview;
    }

    public Optional<URI> getPosterUri() {
        return posterUri;
    }

}
