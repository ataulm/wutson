package com.ataulm.wutson.model;

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

    public boolean contains(String showId) {
        for (ShowSummary showSummary : showSummaries) {
            if (showSummary.getId().equals(showId)) {
                return true;
            }
        }
        return false;
    }

}