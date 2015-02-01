package com.ataulm.wutson;

enum TopLevelNavigationItem {

    BROWSE_SHOWS("Browse shows"),
    SETTINGS("Settings");

    private final String title;

    TopLevelNavigationItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
