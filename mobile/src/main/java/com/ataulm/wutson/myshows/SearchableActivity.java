package com.ataulm.wutson.myshows;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.ataulm.wutson.Log;
import com.ataulm.wutson.R;
import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.navigation.WutsonActivity;
import com.ataulm.wutson.rx.LoggingObserver;
import com.ataulm.wutson.shows.myshows.SearchTvResults;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchableActivity extends WutsonActivity {

    private Subscription subscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shows);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchFor(query);
        }
    }

    private void searchFor(String query) {
        Observable<SearchTvResults> searchTvResultsObservable = Jabber.searchRepository().searchFor(query);
        subscription = searchTvResultsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer(Jabber.log()));
    }

    @Override
    protected void onDestroy() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    private class Observer extends LoggingObserver<SearchTvResults> {

        public Observer(Log log) {
            super(log);
        }

        @Override
        public void onNext(SearchTvResults searchTvResults) {
            super.onNext(searchTvResults);
        }

    }

}
