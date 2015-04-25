package com.ataulm.wutson.myshows;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ataulm.wutson.R;
import com.ataulm.wutson.model.ShowSummary;
import com.ataulm.wutson.view.ShowSummaryView;

import java.util.List;

class TrackedShowsAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;

    private List<ShowSummary> showSummaries;

    TrackedShowsAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    void update(List<ShowSummary> showSummaries) {
        this.showSummaries = showSummaries;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.view_tracked_shows_item, parent, false);
        }
        bindView(position, (ShowSummaryView) view);
        return view;
    }

    private void bindView(int position, ShowSummaryView view) {
        ShowSummary show = getItem(position);
        view.setTitle(show.getName());
        view.setPoster(show.getPosterUri());
    }

}
