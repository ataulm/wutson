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
import rx.schedulers.Schedulers;

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

        subscription = Jabber.discoverShowsRepository().getDiscoverShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DiscoverShowsObserver(presenter));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_discover, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.discover_menu_item_refresh) {
            unsubscribeFrom(subscription);
            subscription = Jabber.discoverShowsRepository().getDiscoverShowsFromNetwork()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DiscoverShowsObserver(presenter));
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (closeNavigationDrawer()) {
            return;
        }
        if (presenter.showFirstPage()) {
            return;
        }
        super.onBackPressed();
    }

}
