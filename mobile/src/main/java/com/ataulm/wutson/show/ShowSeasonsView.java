package com.ataulm.wutson.show;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import java.util.List;

public class ShowSeasonsView extends RecyclerView {

    public ShowSeasonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void display(List<Show.Season> seasons, OnClickSeasonListener listener) {
        if (seasons.isEmpty()) {
            return;
        }

        RecyclerView.Adapter seasonsAdapter = new SeasonsAdapter(LayoutInflater.from(getContext()), seasons, listener);
        setAdapter(seasonsAdapter);
    }

}
