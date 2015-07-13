package com.ataulm.wutson.discover;

import com.ataulm.wutson.shows.ShowId;

interface DiscoverShowSummary {

    ShowId getId();

    String getName();

    String getPosterUrl();

    boolean isTracked();

}
