package com.ataulm.wutson;

import com.ataulm.wutson.tmdb.discovertv.DiscoverTv;
import com.ataulm.wutson.tmdb.discovertv.DiscoverTvJsonParser;
import com.ataulm.wutson.tmdb.discovertv.MockDiscoverTv;

import java.util.ArrayList;
import java.util.List;

public class ShowsProvider implements Provider<List<Show>> {

    @Override
    public List<Show> provide() {
        DiscoverTvJsonParser parser = DiscoverTvJsonParser.newInstance();
        DiscoverTv discoverTv = parser.parse(MockDiscoverTv.JSON);

        List<DiscoverTv.Show> discoverTvShows = discoverTv.getShows();
        List<Show> shows = new ArrayList<>(discoverTvShows.size());

        for (DiscoverTv.Show discoverTvShow : discoverTvShows) {
            shows.add(Show.from(discoverTvShow));
        }

        return shows;
    }

}
