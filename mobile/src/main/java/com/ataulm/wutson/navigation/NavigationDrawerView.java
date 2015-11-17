package com.ataulm.wutson.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;

public class NavigationDrawerView extends LinearLayout {

    private final LayoutInflater layoutInflater;

    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        super.setOrientation(VERTICAL);
        View.inflate(getContext(), R.layout.merge_navigation_drawer, this);
    }

    @Override
    public final void setOrientation(int orientation) {
        throw DeveloperError.methodCannotBeCalledOutsideThisClass();
    }

    public void setupDrawerWith(OnNavigationClickListener clickListener, NavigationDrawerItem currentItem) {
        for (NavigationDrawerItem item : NavigationDrawerItem.values()) {
            addItem(clickListener, currentItem, item);
        }
    }

    private void addItem(OnNavigationClickListener clickListener, NavigationDrawerItem currentItem, NavigationDrawerItem item) {
        if (item == NavigationDrawerItem.SEPARATOR) {
            addSeparatorView();
        } else {
            addActionableItemView(clickListener, currentItem, item);
        }
    }

    private void addSeparatorView() {
        layoutInflater.inflate(R.layout.view_navigation_drawer_item_separator, this, true);
    }

    private void addActionableItemView(final OnNavigationClickListener onNavigationClickListener, NavigationDrawerItem currentItem, final NavigationDrawerItem item) {
        NavigationDrawerItemView itemView = (NavigationDrawerItemView) layoutInflater.inflate(R.layout.view_navigation_drawer_item, this, false);
        itemView.bind(item);
        itemView.setActivated(item == currentItem);
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onNavigationClickListener.onNavigationClick(item);
            }

        });

        addView(itemView);
    }

    public interface OnNavigationClickListener {

        void onNavigationClick(NavigationDrawerItem item);

    }

}
