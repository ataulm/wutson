package com.ataulm.wutson.popularshows;

import com.ataulm.wutson.Fetcher;
import com.ataulm.wutson.JsonParser;
import com.ataulm.wutson.tmdb.configuration.Configuration;
import com.ataulm.wutson.tmdb.configuration.ConfigurationJsonParser;
import com.ataulm.wutson.tmdb.configuration.MockConfiguration;
import com.ataulm.wutson.tmdb.discovertv.DiscoverTv;
import com.ataulm.wutson.tmdb.discovertv.DiscoverTvJsonParser;
import com.ataulm.wutson.tmdb.discovertv.MockDiscoverTv;

import java.util.ArrayList;
import java.util.List;

public class PopularShowsFetcher implements Fetcher<PopularShows> {

    @Override
    public PopularShows fetch() {
        JsonParser<DiscoverTv> parser = DiscoverTvJsonParser.newInstance();
        DiscoverTv discoverTv = parser.parse(MockDiscoverTv.JSON);

        JsonParser<Configuration> configurationJsonParser = ConfigurationJsonParser.newInstance();
        Configuration configuration = configurationJsonParser.parse(MockConfiguration.JSON);

        List<DiscoverTv.Show> discoverTvShows = discoverTv.getShows();
        List<PopularShow> listOfPopularShows = new ArrayList<>(discoverTvShows.size());

        for (DiscoverTv.Show discoverTvShow : discoverTvShows) {
            listOfPopularShows.add(PopularShow.from(discoverTvShow, configuration));
        }

        return new PopularShows(listOfPopularShows);
    }

}
