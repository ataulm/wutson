package com.ataulm.wutson.navigation;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;
import com.ataulm.wutson.auth.WutsonAccountManager;
import com.ataulm.wutson.jabber.Jabber;

public abstract class WutsonTopLevelActivity extends WutsonActivity {

    private static final int DRAWER_GRAVITY = GravityCompat.START;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ViewGroup content;
    private NavigationDrawerView navigationDrawerView;
    private WutsonAccountManager accountManager;

    protected abstract NavigationDrawerItem getNavigationDrawerItem();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager = WutsonAccountManager.newInstance(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_top_level);
        content = (ViewGroup) findViewById(R.id.top_level_container_activity_layout);
        content.removeAllViews();
        getLayoutInflater().inflate(layoutResID, content);

        populateNavigationDrawer();
    }

    @Override
    protected int getNavigationIcon() {
        return R.drawable.ic_action_drawer;
    }

    private void populateNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_drawer_open_content_description, R.string.nav_drawer_close_content_description);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                actionBarDrawerToggle.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                actionBarDrawerToggle.onDrawerOpened(drawerView);
                content.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                actionBarDrawerToggle.onDrawerClosed(drawerView);
                content.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                getToolbar().requestFocus();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                actionBarDrawerToggle.onDrawerStateChanged(newState);
            }

        });

        navigationDrawerView = (NavigationDrawerView) drawerLayout.findViewById(R.id.drawer_list);
        navigationDrawerView.setupDrawerWith(new NavigationDrawerView.OnNavigationClickListener() {

            @Override
            public void onSignInClick() {
                if (userIsSignedIn()) {
                    accountManager.startAddAccountProcess(WutsonTopLevelActivity.this);
                }
            }

            private boolean userIsSignedIn() {
                return accountManager.getAccount() != null;
            }

            @Override
            public void onSignOutClick() {
                if (userIsSignedIn()) {
                    accountManager.signOut();
                    resetNavigationDrawerAccountName();
                }
            }

            @Override
            public void onNavigationClick(NavigationDrawerItem item) {
                closeDrawer();
                if (item == getNavigationDrawerItem()) {
                    return;
                }

                switch (item) {
                    case DISCOVER_SHOWS:
                        navigate().toDiscover();
                        break;
                    case SETTINGS:
                        navigate().toSettings();
                        break;
                    case HELP_FEEDBACK:
                        navigate().toHelpAndFeedback();
                        break;
                    default:
                        onNotImplementedActionFor(item);
                }
            }

            private void onNotImplementedActionFor(NavigationDrawerItem item) {
                String title = item.getTitle();
                Jabber.toastDisplayer().display(title);
            }

        }, getNavigationDrawerItem());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accountManager.getAccount() != null) {
            navigationDrawerView.setAccountName("Signed in as: " + accountManager.getAccount().name);
        } else {
            resetNavigationDrawerAccountName();
        }
    }

    private void resetNavigationDrawerAccountName() {
        navigationDrawerView.setAccountName("Sign in");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(DRAWER_GRAVITY);
            return true;
        }
        throw new IllegalArgumentException("Item id not implemented");
    }

    @Override
    public void onBackPressed() {
        if (closeNavigationDrawer()) {
            return;
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Closes the navigation drawer if it's open.
     *
     * @return true if navigation drawer was successfully closed, false if it wasn't open anyway
     */
    protected boolean closeNavigationDrawer() {
        if (drawerLayout.isDrawerOpen(DRAWER_GRAVITY)) {
            closeDrawer();
            return true;
        } else {
            return false;
        }
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(DRAWER_GRAVITY);
    }

}
