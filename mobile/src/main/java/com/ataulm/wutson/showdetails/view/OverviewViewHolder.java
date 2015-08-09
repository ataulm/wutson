package com.ataulm.wutson.showdetails.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ataulm.wutson.R;

final class OverviewViewHolder extends DetailsViewHolder {

    public static OverviewViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.view_show_details_item_overview, parent, false);
        return new OverviewViewHolder(view);
    }

    private OverviewViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Detail detail) {
        String overview = ((OverviewDetail) detail).getOverview();
        ((TextView) itemView).setText(overview);
        itemView.setBackgroundColor(Color.WHITE);
    }

}
