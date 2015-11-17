package com.ataulm.wutson.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;

public class NavigationDrawerView extends LinearLayout {

    private final LayoutInflater layoutInflater;
    private TextView login;
    private OnNavigationClickListener clickListener;

    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        super.setOrientation(VERTICAL);
        View.inflate(getContext(), R.layout.merge_navigation_drawer, this);

        login = (TextView) findViewById(R.id.navigation_drawer_header_name);
    }

    @Override
    public final void setOrientation(int orientation) {
        throw DeveloperError.methodCannotBeCalledOutsideThisClass();
    }

    public void setupDrawerWith(OnNavigationClickListener clickListener, NavigationDrawerItem currentItem) {
        this.clickListener = clickListener;
        setLoginClickListener(clickListener);
        for (NavigationDrawerItem item : NavigationDrawerItem.values()) {
            addItem(currentItem, item);
        }
    }

    private void setLoginClickListener(final OnNavigationClickListener clickListener) {
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onSignInClick();
            }
        });

        login.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clickListener.onSignOutClick();
                return true;
            }
        });
    }

    private void addItem(NavigationDrawerItem currentItem, NavigationDrawerItem item) {
        if (item == NavigationDrawerItem.SEPARATOR) {
            addSeparatorView();
        } else {
            addActionableItemView(currentItem, item);
        }
    }

    private void addSeparatorView() {
        layoutInflater.inflate(R.layout.view_navigation_drawer_item_separator, this, true);
    }

    private void addActionableItemView(NavigationDrawerItem currentItem, final NavigationDrawerItem item) {
        NavigationDrawerItemView itemView = (NavigationDrawerItemView) layoutInflater.inflate(R.layout.view_navigation_drawer_item, this, false);
        itemView.bind(item);
        itemView.setActivated(item == currentItem);
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clickListener.onNavigationClick(item);
            }

        });

        addView(itemView);
    }

    public void setAccountName(String accountName) {
        login.setText(accountName);
    }

    public interface OnNavigationClickListener {

        void onSignInClick();

        void onSignOutClick();

        void onNavigationClick(NavigationDrawerItem item);

    }

}
