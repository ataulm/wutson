package com.ataulm.wutson.search;

import com.ataulm.wutson.shows.myshows.SearchResult;
import com.ataulm.wutson.trakt.GsonEpisodeIds;
import com.ataulm.wutson.trakt.GsonImage;
import com.ataulm.wutson.trakt.GsonSearchResult;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ConverterShould {

    private static final String DEFAULT_TITLE = "any_title";
    private static final String DEFAULT_ID = "any_id";

    Converter converter = new Converter();

    @Test
    public void returnNullWhenTitleIsNull() {
        GsonSearchResult gsonResult = givenGsonResultWithTitle(null);
        SearchResult result = converter.convert(gsonResult);
        assertThat(result).isNull();
    }

    @Test
    public void returnNullWhenTitleIsEmpty() {
        GsonSearchResult gsonResult = givenGsonResultWithTitle("");
        SearchResult result = converter.convert(gsonResult);
        assertThat(result).isNull();
    }

    @Test
    public void returnNullWhenIdIsNull() {
        GsonSearchResult gsonResult = givenGsonResultWithId(null);
        SearchResult result = converter.convert(gsonResult);
        assertThat(result).isNull();
    }

    @Test
    public void returnNullWhenIdIsEmpty() {
        GsonSearchResult gsonResult = givenGsonResultWithId("");
        SearchResult result = converter.convert(gsonResult);
        assertThat(result).isNull();
    }

    private GsonSearchResult givenGsonResultWithId(String id) {
        return gsonResult(id, DEFAULT_TITLE);
    }

    private static GsonSearchResult givenGsonResultWithTitle(String title) {
        return gsonResult(DEFAULT_ID, title);
    }

    private static GsonSearchResult gsonResult(String traktId, String title) {
        GsonSearchResult result = new GsonSearchResult();
        result.show = new GsonSearchResult.Show();
        result.show.title = title;
        result.show.overview = "overview for " + title;

        result.show.ids = new GsonEpisodeIds();
        result.show.ids.imdb = "imdb id";
        result.show.ids.trakt = traktId;

        result.show.images = new GsonSearchResult.Show.Images();
        result.show.images.poster = new GsonImage();
        result.show.images.poster.full = "www.foo.com/foo_full.jpg";
        result.show.images.poster.medium = "www.foo.com/foo_medium.jpg";
        result.show.images.poster.thumb = "www.foo.com/foo_thumb.jpg";
        return result;
    }

}
