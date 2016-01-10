package com.ataulm.wutson.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.ataulm.wutson.R;
import com.ataulm.wutson.ToastDisplayer;
import com.ataulm.wutson.navigation.WutsonActivity;

public class SearchActivity extends WutsonActivity {

    private ToastDisplayer toaster;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toaster = new ToastDisplayer(this);
        handleIntent(getIntent());
    }

    @Override
    protected boolean hasNoAppBar() {
        return true;
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            toaster.display("query: " + query);
        }
    }

}
