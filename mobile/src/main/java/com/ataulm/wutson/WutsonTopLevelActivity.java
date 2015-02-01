package com.ataulm.wutson;

import android.view.ViewGroup;

public abstract class WutsonTopLevelActivity extends WutsonActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_top_level);
        ViewGroup container = (ViewGroup) findViewById(R.id.top_level_container_activity_layout);
        container.removeAllViews();
        getLayoutInflater().inflate(layoutResID, container);
    }

}
