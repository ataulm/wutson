package com.ataulm.mystories;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.ataulm.mystories.discovertv.DiscoverTv;
import com.ataulm.mystories.discovertv.DiscoverTvParser;

public class DiscoverTvActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DiscoverTvParser parser = DiscoverTvParser.newInstance();
        DiscoverTv discoverTv = parser.parse();
        Log.e("","");
    }

}
