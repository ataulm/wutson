package com.ataulm.wutson.discover;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.vpa.ViewPagerAdapterState;

import java.util.Map;
import java.util.WeakHashMap;

abstract class ViewPagerAdapter extends PagerAdapter {

    private final Map<View, Integer> instantiatedViews = new WeakHashMap<>();

    private int position = PagerAdapter.POSITION_NONE;
    private ViewPagerAdapterState viewPagerAdapterState = ViewPagerAdapterState.newInstance();

    @Override
    public final View instantiateItem(ViewGroup container, int position) {
        View view = getView(container, position);
        view.setId(position);
        instantiatedViews.put(view, position);
        restoreViewState(position, view);
        container.addView(view);
        return view;
    }

    /**
     * Inflate and bind data to the view representing an item at the given position.
     * <p/>
     * Do not add the view to the container, this is handled.
     *
     * @param container the parent view from which sizing information can be grabbed during inflation
     * @param position  the position of the dataset that is to be represented by this view
     * @return the inflated and data-binded view
     */
    protected abstract View getView(ViewGroup container, int position);

    private void restoreViewState(int position, View view) {
        SparseArray<Parcelable> parcelableSparseArray = viewPagerAdapterState.get(position);
        if (parcelableSparseArray == null) {
            return;
        }
        view.restoreHierarchyState(parcelableSparseArray);
    }

    @Override
    public final void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        saveViewState(position, view);
        container.removeView(view);
    }

    private void saveViewState(int position, View view) {
        SparseArray<Parcelable> viewState = new SparseArray<>();
        view.saveHierarchyState(viewState);
        viewPagerAdapterState.put(position, viewState);
    }

    @Override
    public Parcelable saveState() {
        for (Map.Entry<View, Integer> entry : instantiatedViews.entrySet()) {
            int position = entry.getValue();
            View view = entry.getKey();
            saveViewState(position, view);
        }
        return viewPagerAdapterState;
    }

    @Override
    public int getItemPosition(Object object) {
        View view = (View) object;
        return instantiatedViews.get(view);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (!(state instanceof ViewPagerAdapterState)) {
            super.restoreState(state, loader);
        } else {
            this.viewPagerAdapterState = ((ViewPagerAdapterState) state);
        }
    }

    @Override
    public final boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void setPrimaryItem(ViewGroup viewPager, int position, Object view) {
        if (this.position != position) {
            this.position = position;
            onPrimaryItemChanged(((ViewPager) viewPager), position, (View) view);
        }
    }

    private void onPrimaryItemChanged(ViewPager viewPager, int position, View view) {
        viewPagerAdapterState.setCurrentPosition(position);
    }

}
