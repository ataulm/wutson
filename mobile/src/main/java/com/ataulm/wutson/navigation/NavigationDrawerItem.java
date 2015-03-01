package com.ataulm.wutson.navigation;

import android.support.annotation.LayoutRes;

import com.ataulm.wutson.R;

enum NavigationDrawerItem {

    MY_SHOWS("My Shows", R.layout.view_navigation_drawer_item_primary),
    DISCOVER_SHOWS("Discover", R.layout.view_navigation_drawer_item_primary),
    SETTINGS("Settings", R.layout.view_navigation_drawer_item_secondary),
    HELP_FEEDBACK("Help & Feedback", R.layout.view_navigation_drawer_item_secondary);

    private final String title;
    private final int layoutResId;

    NavigationDrawerItem(String title, @LayoutRes int layoutResId) {
        this.title = title;
        this.layoutResId = layoutResId;
    }

    public String getTitle() {
        return title;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

}
