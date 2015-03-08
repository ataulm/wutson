package com.ataulm.wutson.navigation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class Navigator {

    private static final Uri BASE_URI = Uri.parse("content://com.ataulm.wutson2");
    private static final String MIME_TYPE_SHOW_ITEM = "vnd.android.cursor.item/vnd.com.ataulm.wutson.show";
    private static final String MIME_TYPE_SEASON_DIR = "vnd.android.cursor.item/vnd.com.ataulm.wutson.season";

    private final Activity activity;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    public void toShow(String showId) {
        Uri uri = BASE_URI.buildUpon()
                .appendPath("show").appendPath(showId)
                .build();

        view(uri, MIME_TYPE_SHOW_ITEM);
    }

    public void toSeason(String showId, int seasonNumber) {
        Uri uri = BASE_URI.buildUpon()
                .appendPath("show").appendPath(showId)
                .appendPath("season").appendPath(String.valueOf(seasonNumber))
                .build();

        view(uri, MIME_TYPE_SEASON_DIR);
    }

    private void view(Uri uri, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(uri, mimeType);

        activity.startActivity(intent);
    }

}
