package com.ataulm.wutson.myshows;

import com.ataulm.wutson.DataSet;
import com.ataulm.wutson.shows.ShowSummaries;
import com.ataulm.wutson.shows.ShowSummary;

public class MyShowsDataSet implements DataSet<ShowSummary> {

    private ShowSummaries showSummaries;

    void update(ShowSummaries showSummaries) {
        this.showSummaries = showSummaries;
    }

    @Override
    public int getItemCount() {
        if (showSummaries == null) {
            return 0;
        }
        return showSummaries.size();
    }

    @Override
    public ShowSummary getItem(int position) {
        return showSummaries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId().hashCode();
    }

}
