package com.ataulm.wutson.shows.myshows;

import com.ataulm.wutson.Optional;
import com.ataulm.wutson.shows.ShowId;
import com.google.auto.value.AutoValue;

import java.net.URI;

@AutoValue
public abstract class SearchResult {

    public static SearchResult newInstance(ShowId id, String name, Optional<String> overview, Optional<URI> posterUri) {
        return new AutoValue_SearchResult(id, name, overview, posterUri);
    }

    SearchResult() {
    }

    public abstract ShowId id();

    public abstract String name();

    public abstract Optional<String> overview();

    public abstract Optional<URI> posterUri();

}
