package com.ataulm.wutson;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class TopLevelNavigationAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final NavigationDrawerView.OnNavigationClickListener listener;

    TopLevelNavigationAdapter(LayoutInflater inflater, NavigationDrawerView.OnNavigationClickListener listener) {
        this.inflater = inflater;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return TopLevelNavigationItem.values().length;
    }

    @Override
    public TopLevelNavigationItem getItem(int position) {
        return TopLevelNavigationItem.values()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.view_top_level_navigation_item, parent, false);
        }

        final TopLevelNavigationItem item = getItem(position);
        ((TextView) view).setText(item.getTitle());
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNavigationClick(item);
            }

        });
        return view;
    }

}
