package com.ataulm.wutson.show;

import com.ataulm.wutson.tmdb.Season;

import java.net.URI;
import java.util.List;

public class Show {

    private final String name;
    private final String overview;
    private final URI posterUri;
    private final Cast cast;
    private final List<Season> seasons;

    public Show(String name, String overview, URI posterUri, Cast cast, List<Season> seasons) {
        this.name = name;
        this.overview = overview;
        this.posterUri = posterUri;
        this.cast = cast;
        this.seasons = seasons;
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

    public List<Season> getSeasons() {
        return seasons;
    }

}
