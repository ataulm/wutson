package com.ataulm.wutson.episodes;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class DetailViewHolder extends RecyclerView.ViewHolder {

    protected DetailViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(Detail detail) {
    }

}
