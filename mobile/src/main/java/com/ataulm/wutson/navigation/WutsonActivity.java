package com.ataulm.wutson.navigation;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ataulm.wutson.R;
import com.ataulm.wutson.ToastDisplayer;
import com.ataulm.wutson.WutsonApplication;
import com.ataulm.wutson.repository.DataRepository;

public abstract class WutsonActivity extends ActionBarActivity {

    private WutsonApplication application;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (WutsonApplication) getApplication();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar == null) {
            throw new IllegalStateException("Toolbar expected in layout with id: R.id.app_bar");
        }
        setAppBar(toolbar);
    }

    protected void setAppBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        super.setSupportActionBar(toolbar);
    }

    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setActionBar(android.widget.Toolbar toolbar) {
        throw new IllegalStateException("I thought we were using support android.support.v7.widget.Toolbar");
    }

    @Nullable
    @Override
    public ActionBar getActionBar() {
        throw new IllegalStateException("I thought we were using support android.support.v7.widget.Toolbar");
    }

    protected DataRepository getDataRepository() {
        return application.getDataRepository();
    }

    protected ToastDisplayer getToaster() {
        return application.getToastDisplayer();
    }

}
