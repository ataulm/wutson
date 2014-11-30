package com.ataulm.wutson;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

class TrendingShowsViewHolder extends RecyclerView.ViewHolder {

    private final TrendingShowsItemView trendingShowsItemView;
    private static Toast toast;

    public TrendingShowsViewHolder(TrendingShowsItemView itemView) {
        super(itemView);
        this.trendingShowsItemView = itemView;
        // TODO: instead, use this as a legacy viewholder, not holderview
    }

    public void present(final Show show) {
        trendingShowsItemView.setContentDescription(show.getName());
        trendingShowsItemView.update(show);
        trendingShowsItemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(v.getContext(), show.getName(), Toast.LENGTH_SHORT);
                toast.show();
//                Activity activity = (Activity) v.getContext();
//                activity.startActivity(new Intent(activity, ShowActivity.class));
            }

        });
    }

}
