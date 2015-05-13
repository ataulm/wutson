package com.ataulm.wutson;

import com.ataulm.wutson.model.ShowSummary;

import java.util.Collections;
import java.util.List;

public class ShowSummaries {

    private final List<ShowSummary> showSummaries;

    public ShowSummaries(List<ShowSummary> showSummaries) {
        this.showSummaries = Collections.unmodifiableList(showSummaries);
    }

    public int size() {
        return showSummaries.size();
    }

    public ShowSummary get(int location) {
        return showSummaries.get(location);
    }

}
