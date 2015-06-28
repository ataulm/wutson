package com.ataulm.wutson.discover;

import com.ataulm.wutson.shows.ShowSummary;

public interface OnShowClickListener {

    void onClick(ShowSummary showSummary);

    void onClickTrack(ShowSummary showSummary);

    void onClickStopTracking(ShowSummary showSummary);

}
