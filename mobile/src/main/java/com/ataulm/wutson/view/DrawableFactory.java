package com.ataulm.wutson.view;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.ataulm.wutson.DeveloperError;

public final class DrawableFactory {

    private DrawableFactory() {
        throw DeveloperError.nonInstantiableClass();
    }

    public static Drawable getDrawableFrom(@NonNull Resources resources, @DrawableRes int resId) {
        if (resId == 0) {
            return null;
        }
        return isLollipop() ? getDrawableLollipop(resources, resId) : getDrawablePreLollipop(resources, resId);
    }

    private static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @SuppressWarnings("deprecation") // getDrawable(int) deprecated since API 21
    private static Drawable getDrawablePreLollipop(@NonNull Resources resources, @DrawableRes int resId) {
        return resources.getDrawable(resId);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable getDrawableLollipop(@NonNull Resources resources, @DrawableRes int resId) {
        return resources.getDrawable(resId, null);
    }

}
