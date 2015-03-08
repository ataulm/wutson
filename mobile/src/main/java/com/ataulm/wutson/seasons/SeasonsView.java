package com.ataulm.wutson.seasons;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;

public class SeasonsView extends LinearLayout {

    private ViewPager pager;
    private PagerSlidingTabStrip tabs;

    public SeasonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.setOrientation(VERTICAL);
        View.inflate(getContext(), R.layout.merge_seasons, this);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.seasons_view_tabs);
        pager = (ViewPager) findViewById(R.id.seasons_view_pager);
    }

    @Override
    public final void setOrientation(int orientation) {
        throw DeveloperError.methodCannotBeCalledOutsideThisClass();
    }

    void display(Seasons seasons) {
        pager.setAdapter(new SeasonsPagerAdapter(seasons));
        tabs.setViewPager(pager);
    }

    private class SeasonsPagerAdapter extends PagerAdapter {

        private final Seasons seasons;

        SeasonsPagerAdapter(Seasons seasons) {
            this.seasons = seasons;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.view_season, container, false);
            ((SeasonView) view).display(seasons.get(position));
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
            Season season = seasons.get(position);
            // TODO: dat literal
            return "SEASON " + season.getSeasonNumber();
        }

    }

}
