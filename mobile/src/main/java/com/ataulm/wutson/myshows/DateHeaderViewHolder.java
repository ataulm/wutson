package com.ataulm.wutson.myshows;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ataulm.wutson.R;
import com.ataulm.wutson.model.SimpleDate;

class DateHeaderViewHolder extends EpisodesByDateItemViewHolder {

    static DateHeaderViewHolder inflate(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_date_header_item, parent, false);
        return new DateHeaderViewHolder(view);
    }

    private DateHeaderViewHolder(View itemView) {
        super(itemView);
    }

    void bind(SimpleDate date) {
        ((TextView) itemView).setText(date.toString());
    }

}
