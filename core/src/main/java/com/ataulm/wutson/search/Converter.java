package com.ataulm.wutson.search;

import com.ataulm.wutson.Optional;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.myshows.SearchTvResult;
import com.ataulm.wutson.trakt.GsonSearchTvResult;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;

class Converter {

    @Nullable
    public SearchTvResult convert(GsonSearchTvResult gsonSearchTvResult) {
        String traktId = gsonSearchTvResult.show.ids.trakt;
        if (nullOrEmpty(traktId)) {
            return null;
        }
        ShowId showId = new ShowId(gsonSearchTvResult.show.ids.trakt);

        String name = gsonSearchTvResult.show.title;
        if (nullOrEmpty(name)) {
            return null;
        }

        Optional<String> overview = Optional.from(gsonSearchTvResult.show.overview);
        Optional<URI> posterUri = posterUri(gsonSearchTvResult.show.images);
        return new SearchTvResult(showId, name, overview, posterUri);
    }

    private boolean nullOrEmpty(@Nullable String input) {
        return input == null || input.isEmpty();
    }

    private Optional<URI> posterUri(GsonSearchTvResult.Show.Images images) {
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
