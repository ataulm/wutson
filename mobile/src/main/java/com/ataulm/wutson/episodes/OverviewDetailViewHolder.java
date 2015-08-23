package com.ataulm.wutson.episodes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ataulm.wutson.R;

public final class OverviewDetailViewHolder extends DetailViewHolder {

    public static OverviewDetailViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.view_episode_details_item_overview, parent, false);
        return new OverviewDetailViewHolder(view);
    }

    private OverviewDetailViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Detail detail) {
        String overview = ((OverviewDetail) detail).getOverview();
        ((TextView) itemView).setText(overview);
    }

}
