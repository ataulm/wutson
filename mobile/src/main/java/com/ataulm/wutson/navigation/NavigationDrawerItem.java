package com.ataulm.wutson.navigation;

import android.support.annotation.DrawableRes;

import com.ataulm.wutson.R;

public enum NavigationDrawerItem {

    MY_SHOWS("My Shows", R.drawable.temp_icon_24),
    DISCOVER_SHOWS("Discover", R.drawable.temp_icon_24),
    SETTINGS("Settings", R.drawable.temp_icon_24),
    HELP_FEEDBACK("Help & Feedback", R.drawable.temp_icon_24);

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
