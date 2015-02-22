package com.ataulm.wutson.show;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.ataulm.wutson.Jabber;
import com.ataulm.wutson.R;

public class ShowView extends FrameLayout {

    private ViewPager pager;
    private PagerSlidingTabStrip tabs;

    public ShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        View.inflate(getContext(), R.layout.merge_show, this);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.show_view_tabs);
        pager = (ViewPager) findViewById(R.id.show_view_pager);
    }

    void display(Show show) {
        // TODO: this might be null - kick off a task to calculate and cache
        Palette.Swatch swatch = Jabber.swatches().get(show.getPosterUri());
        if (swatch != null) {
            tabs.setBackgroundColor(swatch.getRgb());
            tabs.setTextColor(swatch.getTitleTextColor());
            tabs.setIndicatorColor(swatch.getTitleTextColor());
        }

        pager.setAdapter(new ShowPagerAdapter(show));
        tabs.setViewPager(pager);
    }

    private class ShowPagerAdapter extends PagerAdapter {

        private final Show show;

        private ShowPagerAdapter(Show show) {
            this.show = show;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view;
            if (position == 0) {
                view = layoutInflater.inflate(R.layout.view_show_overview, container, false);
                ((ShowOverviewView) view).display(show);
            } else {
                view = layoutInflater.inflate(R.layout.view_show_seasons, container, false);
                ((ShowSeasonsView) view).display(show.getSeasons());
            }
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            if (show.getSeasons().isEmpty()) {
                return 1;
            } else {
                return 2;
            }
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
                return "OVERVIEW";
            } else {
                return "SEASONS";
            }
        }

    }

}
