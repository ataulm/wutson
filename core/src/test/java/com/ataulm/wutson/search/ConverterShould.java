package com.ataulm.wutson.search;

import com.ataulm.wutson.shows.myshows.SearchTvResult;
import com.ataulm.wutson.trakt.GsonEpisodeIds;
import com.ataulm.wutson.trakt.GsonImage;
import com.ataulm.wutson.trakt.GsonSearchTvResult;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ConverterShould {

    Converter converter = new Converter();

    @Test
    public void returnNullWhenTitleIsNull() {
        GsonSearchTvResult gsonResult = givenGsonResultWithTitle(null);
        SearchTvResult result = converter.convert(gsonResult);
        assertThat(result).isNull();
    }

    @Test
    public void returnNullWhenTitleIsEmpty() {
        GsonSearchTvResult gsonResult = givenGsonResultWithTitle("");
        SearchTvResult result = converter.convert(gsonResult);
        assertThat(result).isNull();
    }

    private static GsonSearchTvResult givenGsonResultWithTitle(String title) {
        GsonSearchTvResult result = new GsonSearchTvResult();
        result.show = new GsonSearchTvResult.Show();
        result.show.title = title;
        result.show.overview = "overview for " + title;

        result.show.ids = new GsonEpisodeIds();
        result.show.ids.imdb = "imdb id";
        result.show.ids.trakt = "trakt id";

        result.show.images = new GsonSearchTvResult.Show.Images();
        result.show.images.poster = new GsonImage();
        result.show.images.poster.full = "www.foo.com/foo_full.jpg";
        result.show.images.poster.medium = "www.foo.com/foo_medium.jpg";
        result.show.images.poster.thumb = "www.foo.com/foo_thumb.jpg";
        return result;
    }

}
