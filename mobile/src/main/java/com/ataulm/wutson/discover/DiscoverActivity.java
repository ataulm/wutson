package com.ataulm.wutson.discover;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ataulm.wutson.R;
import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.navigation.NavigationDrawerItem;
import com.ataulm.wutson.navigation.WutsonTopLevelActivity;
import com.ataulm.wutson.shows.ShowId;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class DiscoverActivity extends WutsonTopLevelActivity {

    private Presenter presenter;
    private Subscription subscription;

    @Override
    protected NavigationDrawerItem getNavigationDrawerItem() {
        return NavigationDrawerItem.DISCOVER_SHOWS;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.discover_label);

        setContentView(R.layout.activity_discover);

        presenter = Presenter.newInstance(
                this,
                new DiscoverNavigator() {
                    @Override
                    public void toShowDetails(ShowId showId, String showTitle, String showBackdropUri, int accentColor) {
                        navigate().toShowDetails(showId, showTitle, showBackdropUri, accentColor);
                    }
                }
        );

        subscription = Jabber.discoverShowsRepository().getDiscoverShowsEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DiscoverShowsEventObserver(presenter));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_discover, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.showTabs();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.discover_menu_item_search:
                presenter.hideTabs(new Runnable() {

                    @Override
                    public void run() {
                        navigate().toSearch();
                    }

                });
                return true;
            case R.id.discover_menu_item_refresh:
                Jabber.discoverShowsRepository().refreshDiscoverShows();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static void unsubscribeFrom(Subscription subscription) {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        unsubscribeFrom(subscription);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        boolean backPressConsumed = closeNavigationDrawer();
        if (backPressConsumed) {
            return;
        }

        backPressConsumed = presenter.showFirstPage();

        if (backPressConsumed) {
            return;
        }

        super.onBackPressed();
    }

}
