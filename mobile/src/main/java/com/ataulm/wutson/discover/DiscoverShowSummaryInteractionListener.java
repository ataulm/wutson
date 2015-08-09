package com.ataulm.wutson.discover;

import android.support.annotation.ColorInt;

import com.ataulm.wutson.shows.ShowSummary;

public interface DiscoverShowSummaryInteractionListener {

    void onClick(ShowSummary showSummary, @ColorInt int accentColor);

}
