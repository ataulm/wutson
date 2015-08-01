package com.ataulm.wutson.myshows;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ataulm.rv.SpacesItemDecoration;
import com.ataulm.wutson.R;
import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.navigation.NavigationDrawerItem;
import com.ataulm.wutson.navigation.WutsonTopLevelActivity;
import com.ataulm.wutson.rx.LoggingObserver;
import com.ataulm.wutson.search.SearchOverlay;
import com.ataulm.wutson.search.SearchSuggestion;
import com.ataulm.wutson.search.SearchSuggestions;
import com.ataulm.wutson.shows.ShowSummaries;
import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.shows.TrackedStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MyShowsActivity extends WutsonTopLevelActivity implements OnShowClickListener {

    private static final List<String> SEARCH_HISTORY = Arrays.asList("lost", "arrow", "modern family", "suits", "lost in oz");
    private static final List<String> API_MATCHES = Arrays.asList(
            "Lost",
            "Lost Girl",
            "Lost in Oz",
            "The Lost Soul",
            "The Lost World",
            "Lost in Space",
            "The Lost Treasure of Aquila",
            "Lost Universe",
            "Lost Christmas",
            "Edens Lost",
            "Lost Highway",
            "Lost Souls",
            "Get Lost!",
            "Lost Tapes",
            "Paradise Lost",
            "Lost Worlds",
            "Heaven's Lost Property",
            "Lost in Austen");
    private static final SearchSuggestions SEARCH_SUGGESTIONS_FROM_DUMMY_HISTORY = createSearchSuggestionsFrom(SEARCH_HISTORY, SearchSuggestion.Type.HISTORY);
    private MyShowsAdapter myShowsAdapter;
    private MyShowsDataSet myShowsDataSet;

    private CompositeSubscription subscriptions;
    private RecyclerView showsListView;
    private SearchOverlay searchOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideTitleWhileWeCheckForTrackedShows();
        setContentView(R.layout.activity_my_shows);

        onCreateSearchOverlay();

        myShowsDataSet = new MyShowsDataSet();
        showsListView = (RecyclerView) findViewById(R.id.my_shows_list);
        int spanCount = getResources().getInteger(R.integer.my_shows_span_count);
        showsListView.setLayoutManager(new GridLayoutManager(this, spanCount));
        int spacing = getResources().getDimensionPixelSize(R.dimen.my_shows_item_spacing);
        showsListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, spanCount));

        subscriptions = new CompositeSubscription(
                Jabber.dataRepository().getMyShows()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new TrackedShowsObserver())
        );
    }

    private void hideTitleWhileWeCheckForTrackedShows() {
        setTitle(null);
    }

    private void onCreateSearchOverlay() {
        searchOverlay = ((SearchOverlay) findViewById(R.id.search_overlay));
        searchOverlay.update(SEARCH_SUGGESTIONS_FROM_DUMMY_HISTORY);
        searchOverlay.setSearchListener(new SearchOverlay.SearchListener() {
            @Override
            public void onQueryUpdated(String query) {
                if (query.isEmpty()) {
                    searchOverlay.update(searchHistoryFilteredBy(query, SEARCH_HISTORY, SearchSuggestion.Type.HISTORY));
                } else {
                    SearchSuggestions searchSuggestions = searchHistoryFilteredBy(query, SEARCH_HISTORY, SearchSuggestion.Type.HISTORY);
                    SearchSuggestions apiSearchSuggestions = searchHistoryFilteredBy(query, API_MATCHES, SearchSuggestion.Type.API);
                    searchOverlay.update(concat(searchSuggestions, apiSearchSuggestions));
                }
            }

            @Override
            public void onQuerySubmitted(String query) {
                navigate().toSearchFor(query);
                searchOverlay.setVisibility(View.GONE);
                Jabber.toastDisplayer().display("onQuerySubmit");
            }
        });
    }

    private SearchSuggestions searchHistoryFilteredBy(String query, List<String> searchTerms, SearchSuggestion.Type type) {
        final List<String> filteredList = new ArrayList<>();
        for (String suggestion : searchTerms) {
            if (suggestion.toLowerCase(Locale.UK).contains(query.toLowerCase(Locale.UK))) {
                filteredList.add(suggestion);
            }
        }
        return createSearchSuggestionsFrom(filteredList, type);
    }

    private SearchSuggestions concat(SearchSuggestions... searchSuggestionses) {
        final List<SearchSuggestion> searchSuggestions = new ArrayList<>();
        for (SearchSuggestions s : searchSuggestionses) {
            for (int i = 0; i < s.getItemCount(); i++) {
                SearchSuggestion item = s.getItem(i);
                searchSuggestions.add(item);
            }
        }
        return new SearchSuggestions() {
            @Override
            public SearchSuggestion getItem(int position) {
                return searchSuggestions.get(position);
            }

            @Override
            public int getItemCount() {
                return searchSuggestions.size();
            }
        };
    }

    private static SearchSuggestions createSearchSuggestionsFrom(final List<String> filteredList, final SearchSuggestion.Type type) {
        return new SearchSuggestions() {
            @Override
            public SearchSuggestion getItem(final int position) {
                return new SearchSuggestion() {
                    @Override
                    public String getName() {
                        return filteredList.get(position);
                    }

                    @Override
                    public Type getType() {
                        return type;
                    }
                };

            }

            @Override
            public int getItemCount() {
                return filteredList.size();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_shows, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.my_shows_menu_item_search) {
            searchOverlay.setVisibility(View.VISIBLE);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        subscriptions.clear();
        super.onDestroy();
    }

    @Override
    protected NavigationDrawerItem getNavigationDrawerItem() {
        return NavigationDrawerItem.MY_SHOWS;
    }

    @Override
    public void onClick(ShowSummary showSummary) {
        navigate().toShowDetails(showSummary.getId(), showSummary.getName(), showSummary.getBackdropUri().toString());
    }

    @Override
    public void onClickStopTracking(ShowSummary showSummary) {
        Jabber.dataRepository().setTrackedStatus(showSummary.getId(), TrackedStatus.NOT_TRACKED);
    }

    @Override
    public void onClickTrack(ShowSummary showSummary) {
        Jabber.dataRepository().setTrackedStatus(showSummary.getId(), TrackedStatus.TRACKED);
    }

    private class TrackedShowsObserver extends LoggingObserver<ShowSummaries> {

        private boolean firstLoad = true;

        private TrackedShowsObserver() {
            super(Jabber.log());
        }

        @Override
        public void onNext(ShowSummaries showSummaries) {
            super.onNext(showSummaries);
            if (nothingToSeeHere(showSummaries)) {
                navigate().toDiscover();
            } else {
                // TODO this is broken - open with no shows -> discover, backs into this activity (should close app)
                firstLoad = false;
                setTitle(R.string.my_shows_label);
                updateList(showSummaries);
            }
        }

        private boolean nothingToSeeHere(ShowSummaries showSummaries) {
            return showSummaries.size() == 0 && activityWasOpenedFromLauncher() && firstLoad;
        }

        private boolean activityWasOpenedFromLauncher() {
            Set<String> categories = getIntent().getCategories();
            return categories != null && categoryLauncherIsIn(categories);
        }

        private boolean categoryLauncherIsIn(Set<String> categories) {
            return categories.contains(Intent.CATEGORY_LAUNCHER) || categoryLeanbackLauncherIsIn(categories);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private boolean categoryLeanbackLauncherIsIn(Set<String> categories) {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && categories.contains(Intent.CATEGORY_LEANBACK_LAUNCHER);
        }

        private void updateList(ShowSummaries showSummaries) {
            if (showsListView.getAdapter() == null) {
                setNewAdapter(showSummaries);
            } else {
                myShowsDataSet.update(showSummaries);
                myShowsAdapter.notifyDataSetChanged();
            }
        }

        private void setNewAdapter(ShowSummaries showSummaries) {
            myShowsDataSet.update(showSummaries);
            myShowsAdapter = new MyShowsAdapter(myShowsDataSet, MyShowsActivity.this, getLayoutInflater());
            myShowsAdapter.setHasStableIds(true);
            showsListView.setAdapter(myShowsAdapter);
        }

    }

}
