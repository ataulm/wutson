package com.ataulm.wutson.seasons;

import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.R;

class SeasonsPagerAdapter extends PagerAdapter {

    private final Seasons seasons;
    private final LayoutInflater layoutInflater;
    private final Resources resources;

    SeasonsPagerAdapter(Seasons seasons, LayoutInflater layoutInflater, Resources resources) {
        this.seasons = seasons;
        this.layoutInflater = layoutInflater;
        this.resources = resources;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        RecyclerView view = (RecyclerView) layoutInflater.inflate(R.layout.view_season_page, container, false);
        Season season = seasons.get(position);
        view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        view.setAdapter(new SeasonAdapter(season, layoutInflater));
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return seasons.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            // TODO: dat direct usage without aliases
            return resources.getString(R.string.specials);
        } else {
            Season season = seasons.get(position);
            // TODO: dat literal
            return "SEASON " + season.getSeasonNumber();
        }
    }

}
