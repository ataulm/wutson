package com.ataulm.wutson.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ataulm.wutson.Log;
import com.ataulm.wutson.R;
import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.navigation.WutsonActivity;
import com.ataulm.wutson.rx.LoggingObserver;
import com.ataulm.wutson.shows.myshows.SearchTvResult;
import com.ataulm.wutson.shows.myshows.SearchTvResults;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchActivity extends WutsonActivity {

    private Subscription subscription;
    private SearchResultsAdapter searchResultsAdapter;
    private SearchOverlay searchOverlay;
    private RecyclerView searchResultsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        onCreateSearchOverlay();

        searchResultsView = (RecyclerView) findViewById(R.id.search_results_list);
        searchResultsView.setLayoutManager(new GridLayoutManager(this, 2));

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchFor(query);
        }
    }

    private void onCreateSearchOverlay() {
        searchOverlay = ((SearchOverlay) findViewById(R.id.search_overlay));
        searchOverlay.setSearchListener(new SearchOverlay.SearchListener() {
            @Override
            public void onQueryUpdated(String query) {
                // TODO: filter search suggestions to ones that match query
            }

            @Override
            public void onQuerySubmitted(String query) {
                navigate().toSearchFor(query);
                searchOverlay.setVisibility(View.GONE);
            }
        });
    }

    private SearchSuggestions dummySearchSuggestions() {
        // TODO: here we can show search history
        return new SearchSuggestions() {
            @Override
            public SearchSuggestion getItem(int position) {
                return null;
            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_menu_item_search) {
            searchOverlay.setVisibility(View.VISIBLE);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private class OnClick implements OnClickSearchTvResult {

        @Override
        public void onClick(SearchTvResult searchTvResult) {
            navigate().toShowDetails(searchTvResult.getId(), searchTvResult.getName(), searchTvResult.getBackdropUri().toString());
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void searchFor(String query) {
        setTitle("\"" + query + "\"");
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
            if (searchResultsView.getAdapter() == null) {
                searchResultsAdapter = new SearchResultsAdapter(new OnClick(), getLayoutInflater());
                searchResultsAdapter.setHasStableIds(true);
                searchResultsAdapter.update(new SearchResultsDataSet(searchTvResults));

                searchResultsView.setAdapter(searchResultsAdapter);
            } else {
                searchResultsAdapter.update(new SearchResultsDataSet(searchTvResults));
            }
        }

    }

}
