package com.ataulm.wutson.navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ataulm.wutson.R;

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
        final TopLevelNavigationItem item = getItem(position);
        View view = convertView;
        if (view == null) {
            view = createNewViewFor(item, parent);
        }
        bindViewTo(item, view);
        return view;
    }

    private View createNewViewFor(TopLevelNavigationItem item, ViewGroup parent) {
        switch (item.getViewType()) {
            case HEADER:
                return inflater.inflate(R.layout.view_top_level_navigation_item_header, parent, false);
            case PRIMARY:
                return inflater.inflate(R.layout.view_top_level_navigation_item_primary, parent, false);
            case SECONDARY:
                return inflater.inflate(R.layout.view_top_level_navigation_item_secondary, parent, false);
            default:
                throw new IllegalArgumentException("not sure of viewtype to bind: " + item);
        }
    }

    private void bindViewTo(TopLevelNavigationItem item, View view) {
        switch (item.getViewType()) {
            case HEADER:
                return;
            case PRIMARY:
            case SECONDARY:
                bindInteractiveViewTo(item, view);
                return;
            default:
                throw new IllegalArgumentException("not sure of viewtype to bind: " + item);
        }
    }

    private void bindInteractiveViewTo(final TopLevelNavigationItem item, View view) {
        ((TextView) view).setText(item.getTitle());

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onNavigationClick(item);
            }

        });
    }

}
