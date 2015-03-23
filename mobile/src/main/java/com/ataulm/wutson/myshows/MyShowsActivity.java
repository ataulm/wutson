package com.ataulm.wutson.myshows;

import android.os.Bundle;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.navigation.NavigationDrawerItem;
import com.ataulm.wutson.navigation.WutsonTopLevelActivity;

public class MyShowsActivity extends WutsonTopLevelActivity {

    private TextView myShowsTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shows);

        myShowsTextView = (TextView) findViewById(R.id.test);
    }

    @Override
    protected NavigationDrawerItem getNavigationDrawerItem() {
        return NavigationDrawerItem.MY_SHOWS;
    }

}
