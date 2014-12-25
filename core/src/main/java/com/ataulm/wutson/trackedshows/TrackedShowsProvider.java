package com.ataulm.wutson.trackedshows;

import com.ataulm.wutson.Provider;
import com.ataulm.wutson.tmdb.discovertv.DiscoverTv;
import com.ataulm.wutson.tmdb.discovertv.DiscoverTvJsonParser;
import com.ataulm.wutson.tmdb.discovertv.MockDiscoverTv;

import java.util.ArrayList;
import java.util.List;

public class TrackedShowsProvider implements Provider<TrackedShows> {

    @Override
    public TrackedShows provide() {
        DiscoverTvJsonParser parser = DiscoverTvJsonParser.newInstance();
        DiscoverTv discoverTv = parser.parse(MockDiscoverTv.JSON);

        List<DiscoverTv.Show> discoverTvShows = discoverTv.getShows();
        List<TrackedShow> listOfTrackedShows = new ArrayList<>(discoverTvShows.size());

        for (DiscoverTv.Show discoverTvShow : discoverTvShows) {
            listOfTrackedShows.add(TrackedShow.from(discoverTvShow));
        }

        return new TrackedShows(listOfTrackedShows);
    }

}
