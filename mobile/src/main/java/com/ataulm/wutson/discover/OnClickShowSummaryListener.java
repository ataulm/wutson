package com.ataulm.wutson.discover;

import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.ShowSummary;

public interface OnClickShowSummaryListener {

    void onClick(ShowSummary showSummary);

    void onClickToggleTrackedStatus(ShowId showId);

}
