package com.ataulm.wutson.seasons;

import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.vpa.ViewPagerAdapter;
import com.ataulm.wutson.R;

class SeasonsPagerAdapter extends ViewPagerAdapter {

    private final Seasons seasons;
    private final OnClickEpisodeListener listener;
    private final LayoutInflater layoutInflater;
    private final Resources resources;

    SeasonsPagerAdapter(Seasons seasons, OnClickEpisodeListener listener, LayoutInflater layoutInflater, Resources resources) {
        this.seasons = seasons;
        this.listener = listener;
        this.layoutInflater = layoutInflater;
        this.resources = resources;
    }

    @Override
    protected View getView(ViewGroup container, int position) {
        RecyclerView view = (RecyclerView) layoutInflater.inflate(R.layout.view_season_page, container, false);
        view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        RecyclerView.Adapter adapter = new SeasonAdapter(seasons.get(position), listener, layoutInflater);
        adapter.setHasStableIds(true);
        view.setAdapter(adapter);
        return view;
    }

    @Override
    public int getCount() {
        return seasons.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Season season = seasons.get(position);
        if (season.getSeasonNumber() == 0) {
            // FIXME: dat direct usage without aliases
            return resources.getString(R.string.specials);
        } else {
            // TODO: dat literal
            return "SEASON " + season.getSeasonNumber();
        }
    }

    int positionOfSeasonNumber(int seasonNumber) {
        for (int i = 0; i < seasons.size(); i++) {
            if (seasons.get(i).getSeasonNumber() == seasonNumber) {
                return i;
            }
        }
        return 0;
    }

}
