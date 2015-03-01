package com.ataulm.wutson.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListAdapter;
import android.widget.ListView;

public class NavigationDrawerView extends ListView {

    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setupWithListener(OnNavigationClickListener listener) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        ListAdapter adapter = new TopLevelNavigationAdapter(layoutInflater, listener);
        super.setAdapter(adapter);
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        throw new IllegalAccessError("You should use setupWithListener instead");
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        throw new IllegalAccessError("The NavDrawerView handles it's own content no need to set an adapter");
    }

    public interface OnNavigationClickListener {

        void onNavigationClick(TopLevelNavigationItem item);

    }

}
