package com.ataulm.wutson.model;

public class GroupedShowSummaries {

    private final String groupHeader;
    private final ShowSummaries showSummaries;

    public GroupedShowSummaries(String groupHeader, ShowSummaries showSummaries) {
        this.groupHeader = groupHeader;
        this.showSummaries = showSummaries;
    }

    public String getGroupHeader() {
        return groupHeader;
    }

    public ShowSummaries getShowSummaries() {
        return showSummaries;
    }

}
