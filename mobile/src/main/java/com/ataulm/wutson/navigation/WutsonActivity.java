package com.ataulm.wutson.navigation;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ataulm.wutson.R;
import com.ataulm.wutson.view.AppBarWidget;

public abstract class WutsonActivity extends ActionBarActivity {

    private AppBarWidget appBarWidget;
    private Navigator navigator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigator = new Navigator(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (hasNoAppBar()) {
            return;
        }

        appBarWidget = (AppBarWidget) findViewById(R.id.app_bar);
        if (appBarWidget == null) {
            throw new IllegalStateException("AppBarWidget expected in layout with id: R.id.app_bar");
        }
        setAppBar(appBarWidget.getToolbar());
    }

    protected Navigator navigate() {
        return navigator;
    }

    protected AppBarWidget getAppBarWidget() {
        return appBarWidget;
    }

    private void setAppBar(Toolbar toolbar) {
        if (hasNoAppBar()) {
            return;
        }
        toolbar.setNavigationIcon(getNavigationIcon());
        super.setSupportActionBar(toolbar);
    }

    protected int getNavigationIcon() {
        return R.drawable.ic_action_back;
    }

    protected Toolbar getToolbar() {
        return appBarWidget.getToolbar();
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

    protected boolean hasNoAppBar() {
        return false;
    }

}
