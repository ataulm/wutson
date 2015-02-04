package com.ataulm.wutson.navigation;

enum TopLevelNavigationItem {

    MY_SHOWS("My Shows"),
    DISCOVER_SHOWS("Discover"),
    SETTINGS("Settings"),
    HELP_FEEDBACK("Help & Feedback");

    private final String title;

    TopLevelNavigationItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
