package com.ataulm.wutson.myshows;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.navigation.NavigationDrawerItem;
import com.ataulm.wutson.navigation.WutsonTopLevelActivity;

import java.util.Set;

public class MyShowsActivity extends WutsonTopLevelActivity {

    private TextView myShowsTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (userHasZeroTrackedShows() && activityWasOpenedFromLauncher()) {
            navigate().toDiscover();
            finish();
            return;
        }

        setContentView(R.layout.activity_my_shows);
        myShowsTextView = (TextView) findViewById(R.id.test);
    }

    private boolean activityWasOpenedFromLauncher() {
        Set<String> categories = getIntent().getCategories();
        return categories != null && categoryLauncherIsIn(categories);
    }

    private boolean categoryLauncherIsIn(Set<String> categories) {
        return categories.contains(Intent.CATEGORY_LAUNCHER) || categoryLeanbackLauncherIsIn(categories);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean categoryLeanbackLauncherIsIn(Set<String> categories) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && categories.contains(Intent.CATEGORY_LEANBACK_LAUNCHER);
    }

    private boolean userHasZeroTrackedShows() {
        // TODO: check from DB / preference
        return true;
    }

    @Override
    protected NavigationDrawerItem getNavigationDrawerItem() {
        return NavigationDrawerItem.MY_SHOWS;
    }

}
