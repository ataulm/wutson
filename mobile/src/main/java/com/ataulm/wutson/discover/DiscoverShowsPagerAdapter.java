package com.ataulm.wutson.discover;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.rv.SpacesItemDecoration;
import com.ataulm.vpa.ViewPagerAdapter;
import com.ataulm.wutson.R;
import com.ataulm.wutson.shows.ShowSummaries;
import com.ataulm.wutson.shows.discover.DiscoverShows;
import com.ataulm.wutson.view.AppBarExpander;

final class DiscoverShowsPagerAdapter extends ViewPagerAdapter {

    private final LayoutInflater layoutInflater;
    private final DiscoverShowSummaryInteractionListener listener;
    private final AppBarExpander appBarExpander;
    private final int spanCount;
    private final int itemSpacingPx;

    private DiscoverShows discoverShows;

    static DiscoverShowsPagerAdapter newInstance(Context context, DiscoverShowSummaryInteractionListener listener, AppBarExpander appBarExpander) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        int spanCount = context.getResources().getInteger(R.integer.discover_shows_span_count);
        int itemSpacingPx = context.getResources().getDimensionPixelSize(R.dimen.discover_shows_item_spacing);
        return new DiscoverShowsPagerAdapter(layoutInflater, listener, appBarExpander, spanCount, itemSpacingPx);
    }

    private DiscoverShowsPagerAdapter(LayoutInflater layoutInflater, DiscoverShowSummaryInteractionListener listener, AppBarExpander appBarExpander, int spanCount, int itemSpacingPx) {
        this.layoutInflater = layoutInflater;
        this.listener = listener;
        this.appBarExpander = appBarExpander;
        this.spanCount = spanCount;
        this.itemSpacingPx = itemSpacingPx;
    }

    public void update(DiscoverShows discoverShows) {
        this.discoverShows = discoverShows;
        notifyDataSetChanged();
    }

    @Override
    protected View getView(ViewGroup viewGroup, int position) {
        final RecyclerView recyclerView = (RecyclerView) layoutInflater.inflate(R.layout.view_discover_page, viewGroup, false);
        recyclerView.setLayoutManager(new FocusBlockingGridLayoutManager(viewGroup.getContext(), spanCount) {
            @Override
            public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
                if (!recyclerView.isInTouchMode()) {
                    onScrollWhenInNonTouchMode(dy);
                }
                return super.scrollVerticallyBy(dy, recycler, state);
            }

            private void onScrollWhenInNonTouchMode(int dy) {
                if (dy > 0) {
                    appBarExpander.collapseAppBar();
                } else {
                    appBarExpander.expandAppBar();
                }
            }
        });
        recyclerView.addItemDecoration(SpacesItemDecoration.newInstance(itemSpacingPx, itemSpacingPx, spanCount));

        ShowSummaries showSummaries = discoverShows.getShowSummaries(position);
        recyclerView.setAdapter(new DiscoverShowAdapter(showSummaries, listener));
        return recyclerView;
    }

    private static class FocusBlockingGridLayoutManager extends GridLayoutManager {

        public FocusBlockingGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        @Override
        public void addView(View child, int index) {
            super.addView(child, index);
            child.setOnKeyListener(
                    new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (event.getAction() != KeyEvent.ACTION_DOWN) {
                                return false;
                            }

                            if (isDpadLeft(keyCode) && isInFirstColumn(v) || isDpadRight(keyCode) && isInLastColumn(v)) {
                                return true;
                            } else {
                                return false;
                            }
                        }

                        private boolean isDpadLeft(int keyCode) {
                            return keyCode == KeyEvent.KEYCODE_DPAD_LEFT;
                        }

                        private boolean isInFirstColumn(View v) {
                            int position = getPosition(v);
                            return getSpanSizeLookup().getSpanIndex(position, getSpanCount()) == 0;
                        }

                        private boolean isDpadRight(int keyCode) {
                            return keyCode == KeyEvent.KEYCODE_DPAD_RIGHT;
                        }

                        private boolean isInLastColumn(View v) {
                            int position = getPosition(v);
                            int spanIndex = getSpanSizeLookup().getSpanIndex(position, getSpanCount());
                            int spanSize = getSpanSizeLookup().getSpanSize(position);
                            return spanIndex + spanSize == getSpanCount();
                        }

                    }
            );
        }

        @Override
        public void removeView(View child) {
            super.removeView(child);
            removeOnKeyListenerFrom(child);
        }

        private void removeOnKeyListenerFrom(View child) {
            if (child == null) {
                return;
            }
            child.setOnKeyListener(null);
        }

        @Override
        public void removeViewAt(int index) {
            super.removeViewAt(index);
            View child = getChildAt(index);
            removeOnKeyListenerFrom(child);
        }
    }

    @Override
    public int getCount() {
        if (discoverShows == null) {
            return 0;
        }
        return discoverShows.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return discoverShows.getTitle(position);
    }
}
