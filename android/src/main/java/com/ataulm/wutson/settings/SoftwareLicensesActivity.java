package com.ataulm.wutson.settings;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;

import com.ataulm.wutson.R;

public class SoftwareLicensesActivity extends ActionBarActivity {

    private static final String URL_LICENSES = "file:///android_asset/licenses.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software_licenses);

        WebView webView = (WebView) findViewById(R.id.software_licenses_web);
        webView.loadUrl(URL_LICENSES);
    }

}
