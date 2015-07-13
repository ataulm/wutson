package com.ataulm.wutson.discover;

import com.ataulm.wutson.DataSet;
import com.ataulm.wutson.shows.ShowId;
import com.ataulm.wutson.shows.ShowSummary;
import com.ataulm.wutson.shows.discover.ShowsInGenre;

import java.util.ArrayList;
import java.util.List;

class DiscoverShowsInGenreDataSet implements DataSet<DiscoverShowSummary> {

    private ShowsInGenre showsInGenre;
    private List<ShowId> trackedShowIds;
    private List<DiscoverShowSummary> discoverShowSummaries;

    void update(ShowsInGenre showsInGenre, List<ShowId> trackedShowIds) {
        this.showsInGenre = showsInGenre;
        this.trackedShowIds = trackedShowIds;
        this.discoverShowSummaries = new ArrayList<>(getItemCount());
    }

    @Override
    public int getItemCount() {
        return showsInGenre.size();
    }

    @Override
    public DiscoverShowSummary getItem(int position) {
        if (position >= discoverShowSummaries.size()) {
            DiscoverShowSummary discoverShowSummary = discoverShowSummaryFrom(showsInGenre.get(position));
            discoverShowSummaries.add(position, discoverShowSummary);
        }
        return discoverShowSummaries.get(position);
    }

    private DiscoverShowSummary discoverShowSummaryFrom(final ShowSummary showSummary) {
        return new DiscoverShowSummary() {
            @Override
            public ShowId getId() {
                return showSummary.getId();
            }

            @Override
            public String getName() {
                return showSummary.getName();
            }

            @Override
            public String getPosterUrl() {
                return showSummary.getPosterUri().toString();
            }

            @Override
            public boolean isTracked() {
                return trackedShowIds.contains(showSummary.getId());
            }
        };
    }

    @Override
    public long getItemId(int position) {
        return showsInGenre.get(position).getId().hashCode();
    }

}
