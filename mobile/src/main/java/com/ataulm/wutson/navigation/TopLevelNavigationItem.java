package com.ataulm.wutson.navigation;

enum TopLevelNavigationItem {

    HEADER("", TopLevelNavigationViewType.HEADER),
    MY_SHOWS("My Shows", TopLevelNavigationViewType.PRIMARY),
    DISCOVER_SHOWS("Discover", TopLevelNavigationViewType.PRIMARY),
    SETTINGS("Settings", TopLevelNavigationViewType.SECONDARY),
    HELP_FEEDBACK("Help & Feedback", TopLevelNavigationViewType.SECONDARY);

    private final String title;
    private final TopLevelNavigationViewType viewType;

    TopLevelNavigationItem(String title, TopLevelNavigationViewType viewType) {
        this.title = title;
        this.viewType = viewType;
    }

    public String getTitle() {
        return title;
    }

    public TopLevelNavigationViewType getViewType() {
        return viewType;
    }

}
