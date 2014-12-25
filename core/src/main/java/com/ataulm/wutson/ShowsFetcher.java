package com.ataulm.wutson;

import com.ataulm.wutson.tmdb.discovertv.DiscoverTv;
import com.ataulm.wutson.tmdb.discovertv.DiscoverTvJsonParser;
import com.ataulm.wutson.tmdb.discovertv.MockDiscoverTv;

import java.util.List;

public class ShowsFetcher implements Fetcher<Shows> {

    @Override
    public Shows fetch() {
        DiscoverTvJsonParser parser = DiscoverTvJsonParser.newInstance();
        DiscoverTv discoverTv = parser.parse(MockDiscoverTv.JSON);

        List<DiscoverTv.Show> discoverTvShows = discoverTv.getShows();

        Shows.Builder showsBuilder = new Shows.Builder();
        for (DiscoverTv.Show discoverTvShow : discoverTvShows) {
            showsBuilder.add(Show.from(discoverTvShow));
        }

        return showsBuilder.toShows();
    }

}
