package com.ataulm.wutson.myshows;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ataulm.wutson.Log;
import com.ataulm.wutson.R;
import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.navigation.WutsonActivity;
import com.ataulm.wutson.rx.LoggingObserver;
import com.ataulm.wutson.search.OnClickSearchTvResult;
import com.ataulm.wutson.search.SearchOverlay;
import com.ataulm.wutson.search.SearchSuggestion;
import com.ataulm.wutson.search.SearchSuggestions;
import com.ataulm.wutson.shows.myshows.SearchTvResult;
import com.ataulm.wutson.shows.myshows.SearchTvResults;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchActivity extends WutsonActivity {

    private Subscription subscription;
    private RecyclerView.Adapter searchResultsAdapter;
    private SearchResultsDataSet dataSet;
    private SearchOverlay searchOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        onCreateSearchOverlay();

        RecyclerView searchResultsListView = (RecyclerView) findViewById(R.id.search_results_list);
        searchResultsListView.setLayoutManager(new LinearLayoutManager(this));
        dataSet = new SearchResultsDataSet();
        searchResultsAdapter = new SearchResultsAdapter(dataSet, new OnClick(), getLayoutInflater());
        searchResultsAdapter.setHasStableIds(true);
        searchResultsListView.setAdapter(searchResultsAdapter);

        handleIntent(getIntent());
    }

    private void onCreateSearchOverlay() {
        searchOverlay = ((SearchOverlay) findViewById(R.id.search_overlay));
        searchOverlay.update(dummySearchSuggestions(), new SearchOverlay.SearchListener() {
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

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchFor(query);
        }
    }

    private void searchFor(String query) {
        setTitle(query);
        Observable<SearchTvResults> searchTvResultsObservable = Jabber.searchRepository().searchFor(query);
        subscription = searchTvResultsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer(Jabber.log()));
    }

    private interface DataSet<T> {

        int getItemCount();

        T getItem(int position);

        long getItemId(int position);

    }

    private static class SearchResultsDataSet implements DataSet<SearchTvResult> {

        private SearchTvResults results;

        void update(SearchTvResults results) {
            this.results = results;
        }

        @Override
        public int getItemCount() {
            return results == null ? 0 : results.size();
        }

        @Override
        public SearchTvResult getItem(int position) {
            return results.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId().hashCode();
        }

    }

    private static class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsViewHolder> {

        private final DataSet<SearchTvResult> dataSet;
        private final OnClickSearchTvResult listener;
        private final LayoutInflater layoutInflater;

        SearchResultsAdapter(DataSet<SearchTvResult> dataSet, OnClickSearchTvResult listener, LayoutInflater layoutInflater) {
            this.dataSet = dataSet;
            this.listener = listener;
            this.layoutInflater = layoutInflater;
        }

        @Override
        public SearchResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return SearchResultsViewHolder.inflate(parent, layoutInflater);
        }

        @Override
        public void onBindViewHolder(SearchResultsViewHolder holder, int position) {
            SearchTvResult item = dataSet.getItem(position);
            holder.bind(item, listener);
        }

        @Override
        public long getItemId(int position) {
            return dataSet.getItemId(position);
        }

        @Override
        public int getItemCount() {
            return dataSet.getItemCount();
        }

    }

    private static final class SearchResultsViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;

        static SearchResultsViewHolder inflate(ViewGroup parent, LayoutInflater layoutInflater) {
            View view = layoutInflater.inflate(R.layout.view_search_result, parent, false);
            TextView nameTextView = (TextView) view.findViewById(R.id.search_result_text_name);
            return new SearchResultsViewHolder(view, nameTextView);
        }

        private SearchResultsViewHolder(View itemView, TextView nameTextView) {
            super(itemView);
            this.nameTextView = nameTextView;
        }

        void bind(final SearchTvResult searchTvResult, final OnClickSearchTvResult listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(searchTvResult);
                }
            });
            nameTextView.setText(searchTvResult.getName());
        }

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
            dataSet.update(searchTvResults);
            searchResultsAdapter.notifyDataSetChanged();
        }

    }

}
