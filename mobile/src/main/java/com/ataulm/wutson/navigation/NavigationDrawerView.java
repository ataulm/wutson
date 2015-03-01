package com.ataulm.wutson.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.wutson.ClassContractBrokenException;
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
    public void setOrientation(int orientation) {
        throw new ClassContractBrokenException();
    }

    public void setupDrawerWith(final OnNavigationClickListener onNavigationClickListener) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (final NavigationDrawerItem item : NavigationDrawerItem.values()) {
            TextView itemView = (TextView) inflater.inflate(item.getLayoutResId(), this, false);
            itemView.setText(item.getTitle());
            itemView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    onNavigationClickListener.onNavigationClick(item);
                }

            });
            addView(itemView);
        }
    }

    public interface OnNavigationClickListener {

        void onNavigationClick(NavigationDrawerItem item);

    }

}
