package com.ataulm.wutson.episodes;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;

import com.ataulm.wutson.BuildConfig;
import com.ataulm.wutson.R;

public final class EpisodeActivityExtras {

    private static final String EXTRA_SHOW_TITLE = BuildConfig.APPLICATION_ID + ".episodes_show_title";
    private static final String EXTRA_SHOW_ACCENT_COLOR = BuildConfig.APPLICATION_ID + ".show_accent_color";

    private final String title;
    @ColorInt
    private final int accentColor;

    static EpisodeActivityExtras from(Intent intent, Resources resources) {
        Bundle bundle = bundleFrom(intent);
        String title = bundle.getString(EXTRA_SHOW_TITLE, resources.getString(R.string.app_name));
        int fallbackColor = resources.getColor(R.color.show_details_app_bar_background);
        int accentColor = bundle.getInt(EXTRA_SHOW_ACCENT_COLOR, fallbackColor);
        return new EpisodeActivityExtras(title, accentColor);
    }

    private static Bundle bundleFrom(Intent intent) {
        if (intent == null || intent.getExtras() == null) {
            return Bundle.EMPTY;
        }
        return intent.getExtras();
    }

    public EpisodeActivityExtras(String title, int accentColor) {
        this.title = title;
        this.accentColor = accentColor;
    }

    public String getTitle() {
        return title;
    }

    @ColorInt
    public int getAccentColor() {
        return accentColor;
    }

    public Bundle asBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_SHOW_TITLE, title);
        bundle.putInt(EXTRA_SHOW_ACCENT_COLOR, accentColor);
        return bundle;
    }

}
