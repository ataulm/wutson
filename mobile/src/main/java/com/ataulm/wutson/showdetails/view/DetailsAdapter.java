package com.ataulm.wutson.showdetails.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ataulm.wutson.DeveloperError;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsViewHolder> {

    private final Details details;
    private final LayoutInflater layoutInflater;

    public DetailsAdapter(Details details, LayoutInflater layoutInflater) {
        this.details = details;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Type type = Type.from(viewType);
        switch (type) {
            case HEADER_SPACE:
                return HeaderSpaceDetailsViewHolder.newInstance(layoutInflater, parent);
            case OVERVIEW:
                return OverviewDetailsViewHolder.newInstance(layoutInflater, parent);
            case CAST_TITLE:
                return CastTitleDetailsViewHolder.newInstance(layoutInflater, parent);
            case CHARACTER:
                return CharacterDetailsViewHolder.newInstance(layoutInflater, parent);
            default:
                throw DeveloperError.because("no viewholder for: " + type);
        }
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {
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
