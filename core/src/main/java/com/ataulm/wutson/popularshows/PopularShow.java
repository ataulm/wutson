package com.ataulm.wutson.popularshows;

import com.ataulm.wutson.tmdb.discovertv.DiscoverTv;

import java.text.DecimalFormat;
import java.text.Format;

public class PopularShow {

    private static final Format VOTE_FORMATTER = new DecimalFormat("#.00");

    private final String id;
    private final String showName;
    private final String voteAverage;
    private final String posterUrl;

    public static PopularShow from(DiscoverTv.Show discoverTvShow) {
        String id = String.valueOf(discoverTvShow.getId());
        String showName = discoverTvShow.getName();
        double voteAverageRaw = discoverTvShow.getVoteAverage();
        String voteAverage = voteAverageRaw == 0 ? "" : VOTE_FORMATTER.format(voteAverageRaw);
        String posterUrl = discoverTvShow.getPosterPath();

        return new PopularShow(id, showName, voteAverage, posterUrl);
    }

    PopularShow(String id, String showName, String voteAverage, String posterUrl) {
        this.id = id;
        this.showName = showName;
        this.voteAverage = voteAverage;
        this.posterUrl = posterUrl;
    }

    public String getId() {
        return id;
    }

    public String getShowName() {
        return showName;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

}
