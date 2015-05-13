package com.ataulm.wutson.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;

public class NavigationDrawerView extends LinearLayout {

    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.setOrientation(VERTICAL);
        View.inflate(getContext(), R.layout.merge_navigation_drawer, this);
    }

    @Override
    public final void setOrientation(int orientation) {
        throw DeveloperError.methodCannotBeCalledOutsideThisClass();
    }

    public void setupDrawerWith(final OnNavigationClickListener onNavigationClickListener, NavigationDrawerItem selectedItem) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (final NavigationDrawerItem item : NavigationDrawerItem.values()) {
            if (item == NavigationDrawerItem.SEPARATOR) {
                inflater.inflate(R.layout.view_navigation_drawer_item_separator, this, true);
                continue;
            }

            NavigationDrawerItemView itemView = (NavigationDrawerItemView) inflater.inflate(R.layout.view_navigation_drawer_item, this, false);
            itemView.display(item);
            itemView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    onNavigationClickListener.onNavigationClick(item);
                }

            });
            itemView.setSelected(item == selectedItem);
            addView(itemView);
        }
    }

    public interface OnNavigationClickListener {

        void onNavigationClick(NavigationDrawerItem item);

    }

}
