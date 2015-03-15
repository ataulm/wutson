package com.ataulm.wutson.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.R;

// not a problem - https://code.google.com/p/android/issues/detail?id=67434
@SuppressLint("Instantiatable")
class ShowView extends LinearLayout {

    private ViewPager pager;
    private PagerSlidingTabStrip tabs;

    public ShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.setOrientation(VERTICAL);
        View.inflate(getContext(), R.layout.merge_show, this);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.show_view_tabs);
        pager = (ViewPager) findViewById(R.id.show_view_pager);
    }

    @Override
    public final void setOrientation(int orientation) {
        throw DeveloperError.methodCannotBeCalledOutsideThisClass();
    }

    void display(Show show, OnClickSeasonListener onSeasonClickListener) {
        pager.setAdapter(new ShowPagerAdapter(show, onSeasonClickListener, LayoutInflater.from(getContext())));
        tabs.setViewPager(pager);
    }

    private class ShowPagerAdapter extends PagerAdapter {

        private final Show show;
        private final OnClickSeasonListener onSeasonClickListener;
        private final LayoutInflater layoutInflater;

        ShowPagerAdapter(Show show, OnClickSeasonListener onSeasonClickListener, LayoutInflater layoutInflater) {
            this.show = show;
            this.onSeasonClickListener = onSeasonClickListener;
            this.layoutInflater = layoutInflater;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            if (position == 0) {
                return instantiateShowOverviewPage(container);
            }
            if (position == 1) {
                return instantiateShowSeasonsPage(container);
            }
            throw DeveloperError.because("ShowView should only have two pages");
        }

        private View instantiateShowOverviewPage(ViewGroup container) {
            ShowOverviewView view = (ShowOverviewView) layoutInflater.inflate(R.layout.view_show_overview, container, false);
            view.display(show);
            container.addView(view);
            return view;
        }

        private View instantiateShowSeasonsPage(ViewGroup container) {
            RecyclerView view = (RecyclerView) layoutInflater.inflate(R.layout.view_show_seasons, container, false);
            view.setLayoutManager(new LinearLayoutManager(getContext()));
            RecyclerView.Adapter seasonsAdapter = new SeasonsAdapter(LayoutInflater.from(getContext()), show.getSeasons(), onSeasonClickListener);
            view.setAdapter(seasonsAdapter);
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
