package com.ataulm.wutson.discover;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.wutson.vpa.ViewStates;

import java.util.Map;
import java.util.WeakHashMap;

abstract class ViewPagerAdapter extends PagerAdapter {

    private final Map<View, Integer> instantiatedViews = new WeakHashMap<>();

    private ViewStates viewStates = ViewStates.newInstance();

    @Override
    public final View instantiateItem(ViewGroup container, int position) {
        View view = getView(container, position);
        instantiatedViews.put(view, position);
        restoreViewState(position, view);
        container.addView(view);
        return view;
    }

    protected abstract View getView(ViewGroup container, int position);

    private void restoreViewState(int position, View view) {
        SparseArray<Parcelable> parcelableSparseArray = viewStates.get(position);
        if (parcelableSparseArray == null) {
            return;
        }
        view.restoreHierarchyState(parcelableSparseArray);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        saveViewState(position, view);
        container.removeView(view);
    }

    private void saveViewState(int position, View view) {
        SparseArray<Parcelable> viewState = new SparseArray<>();
        view.saveHierarchyState(viewState);
        viewStates.put(position, viewState);
    }

    @Override
    public Parcelable saveState() {
        for (Map.Entry<View, Integer> entry : instantiatedViews.entrySet()) {
            int position = entry.getValue();
            View view = entry.getKey();
            saveViewState(position, view);
        }
        return viewStates;
    }

    @Override
    public int getItemPosition(Object object) {
        View view = (View) object;
        return instantiatedViews.get(view);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (!(state instanceof ViewStates)) {
            super.restoreState(state, loader);
        } else {
            this.viewStates = ((ViewStates) state);
            notifyDataSetChanged(); // TODO: is this needed? not if `restoreState()` is called before `instantiateItem()`
        }
    }

}
