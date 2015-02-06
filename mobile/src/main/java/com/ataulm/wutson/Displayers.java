package com.ataulm.wutson;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class Displayers {

    @SuppressWarnings("unchecked")
    public static <T> Displayer<T> findById(ViewGroup rootViewGroup, @IdRes int id) {
        return (Displayer<T>) rootViewGroup.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public static <T> Displayer<T> findById(Activity activity, @IdRes int id) {
        return (Displayer<T>) activity.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public static <T> Displayer<T> inflateFromLayout(LayoutInflater layoutInflater, ViewGroup container, @LayoutRes int layout) {
        return inflateFromLayout(layoutInflater, container, layout, false);
    }

    @SuppressWarnings("unchecked")
    private static <T> Displayer<T> inflateFromLayout(LayoutInflater layoutInflater, ViewGroup container, @LayoutRes int layout, boolean attachToRoot) {
        return (Displayer<T>) layoutInflater.inflate(layout, container, attachToRoot);
    }

}
