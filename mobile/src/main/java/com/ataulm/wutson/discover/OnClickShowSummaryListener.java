package com.ataulm.wutson.discover;

import com.ataulm.wutson.model.ShowId;
import com.ataulm.wutson.model.ShowSummary;

public interface OnClickShowSummaryListener {

    void onClick(ShowSummary showSummary);

    void onClickToggleTrackedStatus(ShowId showId);

}
