package com.ataulm.wutson.myshows;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private RecyclerView.Adapter searchResultsAdapter;
    private SearchResultsDataSet dataSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView searchResultsListView = (RecyclerView) findViewById(R.id.search_results_list);
        searchResultsListView.setLayoutManager(new LinearLayoutManager(this));
        dataSet = new SearchResultsDataSet();
        searchResultsAdapter = new SearchResultsAdapter(dataSet, getLayoutInflater());
        searchResultsAdapter.setHasStableIds(true);
        searchResultsListView.setAdapter(searchResultsAdapter);

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
        private final LayoutInflater layoutInflater;

        SearchResultsAdapter(DataSet<SearchTvResult> dataSet, LayoutInflater layoutInflater) {
            this.dataSet = dataSet;
            this.layoutInflater = layoutInflater;
        }

        @Override
        public SearchResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return SearchResultsViewHolder.inflate(parent, layoutInflater);
        }

        @Override
        public void onBindViewHolder(SearchResultsViewHolder holder, int position) {
            SearchTvResult item = dataSet.getItem(position);
            holder.bind(item);
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

        void bind(SearchTvResult searchTvResult) {
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
