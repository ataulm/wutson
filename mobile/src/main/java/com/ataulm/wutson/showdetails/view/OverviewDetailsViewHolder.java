package com.ataulm.wutson.showdetails.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ataulm.wutson.R;

final class OverviewDetailsViewHolder extends DetailsViewHolder {

    public static OverviewDetailsViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.view_show_details_item_overview, parent, false);
        return new OverviewDetailsViewHolder(view);
    }

    private OverviewDetailsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Detail detail) {
        // TODO: this shouldn't be necessary - someone is messing with the R.color.white somewhere in the app so it makes it black
        int backgroundColor = itemView.getResources().getColor(R.color.show_details_overview_background);
        itemView.setBackgroundColor(backgroundColor);

        String overview = ((OverviewDetail) detail).getOverview();
        ((TextView) itemView).setText(overview);
    }

}
