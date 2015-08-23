package com.ataulm.wutson.episodes;

import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ataulm.wutson.DeveloperError;

public class EpisodeDetailsAdapter extends RecyclerView.Adapter<DetailViewHolder> {

    private final Details details;
    private final LayoutInflater layoutInflater;

    @ColorInt
    private final int accentColor;

    public EpisodeDetailsAdapter(Details details, LayoutInflater layoutInflater, @ColorInt int accentColor) {
        this.details = details;
        this.layoutInflater = layoutInflater;
        this.accentColor = accentColor;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Type type = Type.from(viewType);
        switch (type) {
            case HEADER_SPACE:
                return HeaderSpaceDetailViewHolder.newInstance(layoutInflater, parent);
            case NAME:
                return NameViewHolder.newInstance(layoutInflater, parent, accentColor);
            case EPISODE_NUMBER:
                return EpisodeNumberViewHolder.newInstance(layoutInflater, parent, accentColor);
            case OVERVIEW:
                return OverviewDetailViewHolder.newInstance(layoutInflater, parent);
            default:
                throw DeveloperError.because("no viewholder for: " + type);
        }
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {
        Detail detail = details.get(position);
        holder.bind(detail);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    @Override
    public int getItemViewType(int position) {
        return details.getType(position).ordinal();
    }

}
