package com.ataulm.wutson;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class Presenters {

    @SuppressWarnings("unchecked")
    public static <T> Presenter<T> findById(ViewGroup rootViewGroup, @IdRes int id) {
        return (Presenter<T>) rootViewGroup.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public static <T> Presenter<T> findById(Activity activity, @IdRes int id) {
        return (Presenter<T>) activity.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public static <T> Presenter<T> inflateFromLayout(LayoutInflater layoutInflater, ViewGroup container, @LayoutRes int layout) {
        return inflateFromLayout(layoutInflater, container, layout, false);
    }

    @SuppressWarnings("unchecked")
    private static <T> Presenter<T> inflateFromLayout(LayoutInflater layoutInflater, ViewGroup container, @LayoutRes int layout, boolean attachToRoot) {
        return (Presenter<T>) layoutInflater.inflate(layout, container, attachToRoot);
    }

}
