package com.ataulm.wutson.myshows;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.model.Episode;
import com.ataulm.wutson.model.EpisodesByDate;
import com.ataulm.wutson.model.SimpleDate;

import java.util.ArrayList;
import java.util.List;

class EpisodesByDateAdapter extends RecyclerView.Adapter<EpisodesByDateItemViewHolder> {

    private final List<EpisodesByDateItem> items;

    EpisodesByDateAdapter() {
        this.items = new ArrayList<>();
    }

    @Override
    public EpisodesByDateItemViewHolder onCreateViewHolder(ViewGroup parent, int viewTypeOrdinal) {
        ViewType viewType = ViewType.values()[viewTypeOrdinal];
        switch (viewType) {
            case DATE:
                return DateHeaderViewHolder.inflate(parent);
            case EPISODE:
                return UpcomingEpisodeViewHolder.inflate(parent);
            default:
                throw DeveloperError.because("unknown view type: " + viewTypeOrdinal);
        }
    }

    @Override
    public void onBindViewHolder(EpisodesByDateItemViewHolder holder, int position) {
        ViewType viewType = ViewType.values()[getItemViewType(position)];
        switch (viewType) {
            case DATE:
                SimpleDate date = (SimpleDate) items.get(position).get();
                ((DateHeaderViewHolder) holder).bind(date);
                break;
            case EPISODE:
                Episode episode = (Episode) items.get(position).get();
                ((UpcomingEpisodeViewHolder) holder).bind(episode);
                break;
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType().ordinal();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void update(EpisodesByDate episodesByDate) {
        items.clear();
        for (final SimpleDate simpleDate : episodesByDate.getDates()) {
            items.add(episodesByDateItemFrom(simpleDate));
            for (Episode episode : episodesByDate.get(simpleDate)) {
                items.add(episodesByDateItemFrom(episode));
            }
        }
        notifyDataSetChanged();
    }

    private static EpisodesByDateItem<SimpleDate> episodesByDateItemFrom(final SimpleDate simpleDate) {
        return new EpisodesByDateItem<SimpleDate>() {
            @Override
            public ViewType getViewType() {
                return ViewType.DATE;
            }

            @Override
            public SimpleDate get() {
                return simpleDate;
            }
        };
    }

    private static EpisodesByDateItem<Episode> episodesByDateItemFrom(final Episode episode) {
        return new EpisodesByDateItem<Episode>() {
            @Override
            public ViewType getViewType() {
                return ViewType.EPISODE;
            }

            @Override
            public Episode get() {
                return episode;
            }
        };
    }

    private enum ViewType {
        DATE,
        EPISODE
    }

    private interface EpisodesByDateItem<T> {

        ViewType getViewType();

        T get();

    }

}
