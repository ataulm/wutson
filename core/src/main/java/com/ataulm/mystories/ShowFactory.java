package com.ataulm.mystories;

import com.ataulm.mystories.tmdb.discovertv.DiscoverTv;

public class ShowFactory {

    public static Show from(DiscoverTv.Show discoverTvShow) {
        Show.Id id = new Show.Id(discoverTvShow.getId());
        Show.Name name = new Show.Name(discoverTvShow.getName());
        Show.PosterPath posterPath = new Show.PosterPath(discoverTvShow.getPosterPath());

        return new Show(id, name, posterPath);
    }

}
