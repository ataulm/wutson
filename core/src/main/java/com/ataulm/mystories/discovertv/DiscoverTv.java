package com.ataulm.mystories.discovertv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiscoverTv {

    private final static DiscoverTv NOTHING_TO_WATCH = new DiscoverTv(Collections.<Show>emptyList());

    private final List<Show> shows;

    public static DiscoverTv empty() {
        return NOTHING_TO_WATCH;
    }

    static DiscoverTv from(GsonDiscoverTv gsonDiscoverTv) {
        List<GsonDiscoverTv.Show> gsonDiscoverTvShows = gsonDiscoverTv.results;
        List<Show> shows = new ArrayList<Show>(gsonDiscoverTvShows.size());
        for (GsonDiscoverTv.Show gsonDiscoverTvShow : gsonDiscoverTvShows) {
            Show show = Show.from(gsonDiscoverTvShow);
            shows.add(show);
        }

        return new DiscoverTv(shows);
    }

    DiscoverTv(List<Show> shows) {
        this.shows = shows;
    }

    public int size() {
        return shows.size();
    }

    static class Show {

        private final int id;
        private final String name;
        private final String posterPath;
        private final String firstAirDate;
        private final double popularity;
        private final double voteAverage;
        private final int voteCount;

        static Show from(GsonDiscoverTv.Show gsonDiscoverTvShow) {
            return new Show(gsonDiscoverTvShow.id,
                    gsonDiscoverTvShow.name,
                    gsonDiscoverTvShow.posterPath,
                    gsonDiscoverTvShow.firstAirDate,
                    gsonDiscoverTvShow.popularity,
                    gsonDiscoverTvShow.voteAverage,
                    gsonDiscoverTvShow.voteCount);
        }

        Show(int id, String name, String posterPath, String firstAirDate, double popularity, double voteAverage, int voteCount) {
            this.id = id;
            this.name = name;
            this.posterPath = posterPath;
            this.firstAirDate = firstAirDate;
            this.popularity = popularity;
            this.voteAverage = voteAverage;
            this.voteCount = voteCount;
        }

    }

}
