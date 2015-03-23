package com.ataulm.wutson.myshows;

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
        return categories != null && categories.contains("android.intent.category.LAUNCHER");
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
