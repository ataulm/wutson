package com.ataulm.wutson.search;

import com.ataulm.wutson.Optional;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.myshows.SearchResult;
import com.ataulm.wutson.trakt.GsonSearchResult;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;

class Converter {

    @Nullable
    public SearchResult convert(GsonSearchResult gsonSearchResult) {
        String traktId = gsonSearchResult.show.ids.trakt;
        if (nullOrEmpty(traktId)) {
            return null;
        }
        ShowId showId = new ShowId(gsonSearchResult.show.ids.trakt);

        String name = gsonSearchResult.show.title;
        if (nullOrEmpty(name)) {
            return null;
        }

        Optional<String> overview = Optional.from(gsonSearchResult.show.overview);
        Optional<URI> posterUri = posterUri(gsonSearchResult.show.images);
        return new SearchResult(showId, name, overview, posterUri);
    }

    private boolean nullOrEmpty(@Nullable String input) {
        return input == null || input.isEmpty();
    }

    private Optional<URI> posterUri(GsonSearchResult.Show.Images images) {
        if (images == null || images.poster == null) {
            return Optional.absent();
        }
        try {
            return Optional.of(new URI(images.poster.full));
        } catch (URISyntaxException e) {
            return Optional.absent();
        }
    }

}
