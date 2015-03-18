package com.ataulm.wutson.navigation;

import android.support.annotation.DrawableRes;

import com.ataulm.wutson.R;

public enum NavigationDrawerItem {

    MY_SHOWS("My Shows", R.drawable.ic_my_shows),
    DISCOVER_SHOWS("Discover", R.drawable.ic_discover),
    SEPARATOR(null, 0),
    SETTINGS("Settings", R.drawable.ic_settings),
    HELP_FEEDBACK("Help & Feedback", R.drawable.ic_help);

    private final String title;
    private final int iconResId;

    NavigationDrawerItem(String title, @DrawableRes int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }

}
