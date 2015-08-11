package com.ataulm.wutson.showdetails;

import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;

import com.ataulm.wutson.R;

enum Page {

    DETAILS(R.layout.view_show_details_details_page, R.string.show_details_pager_title_details),
    SEASONS(R.layout.view_show_details_seasons_page, R.string.show_details_pager_title_seasons);

    private final int layoutResId;
    private final int titleResId;

    Page(@LayoutRes int layoutResId, @StringRes int titleResId) {
        this.layoutResId = layoutResId;
        this.titleResId = titleResId;
    }

    int getLayoutResId() {
        return layoutResId;
    }

    int getTitleResId() {
        return titleResId;
    }

    static Page at(int position) {
        return values()[position];
    }

}
