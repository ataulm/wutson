package com.ataulm.wutson.myshows;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.navigation.WutsonActivity;

public class SearchableActivity extends WutsonActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Jabber.toastDisplayer().display("query: " + query);
        }
    }

}
