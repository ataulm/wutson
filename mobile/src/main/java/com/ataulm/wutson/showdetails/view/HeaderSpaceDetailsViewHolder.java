package com.ataulm.wutson.showdetails.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;

final class HeaderSpaceDetailsViewHolder extends DetailsViewHolder {

    public static HeaderSpaceDetailsViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.view_show_details_item_header_space, parent, false);
        return new HeaderSpaceDetailsViewHolder(view);
    }

    private HeaderSpaceDetailsViewHolder(View itemView) {
        super(itemView);
    }

}
