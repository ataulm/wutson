package com.ataulm.wutson.showdetails.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;

public final class CastTitleViewHolder extends DetailsViewHolder {

    public static CastTitleViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.view_show_details_item_cast_title, parent, false);
        return new CastTitleViewHolder(view);
    }

    private CastTitleViewHolder(View itemView) {
        super(itemView);
    }

}
