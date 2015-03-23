package com.ataulm.wutson.navigation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.ataulm.wutson.showdetails.ShowDetailsActivity;

public class Navigator {

    private static final Uri BASE_URI = Uri.parse("content://com.ataulm.wutson2");
    private static final String MIME_TYPE_SHOW_ITEM = "vnd.android.cursor.item/vnd.com.ataulm.wutson.show";
    private static final String MIME_TYPE_SEASON_DIR = "vnd.android.cursor.item/vnd.com.ataulm.wutson.season";
    private static final Uri WUTSON_GPLUS_COMMUNITY = Uri.parse("https://plus.google.com/communities/111719082560247438789");

    private final Activity activity;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    public void toShow(String showId, String showTitle, String showBackdropUri) {
        Uri uri = BASE_URI.buildUpon()
                .appendPath("show").appendPath(showId)
                .build();

        start(view(uri, MIME_TYPE_SHOW_ITEM)
                .putExtra(ShowDetailsActivity.EXTRA_SHOW_TITLE, showTitle)
                .putExtra(ShowDetailsActivity.EXTRA_SHOW_BACKDROP, showBackdropUri));
    }

    public void toSeason(String showId, int seasonNumber) {
        Uri uri = BASE_URI.buildUpon()
                .appendPath("show").appendPath(showId)
                .appendPath("season").appendPath(String.valueOf(seasonNumber))
                .build();

        start(view(uri, MIME_TYPE_SEASON_DIR));
    }

    public void toWutsonGooglePlusCommunity() {
        start(view(WUTSON_GPLUS_COMMUNITY));
    }

    private Intent view(Uri uri) {
        return view(uri, null);
    }

    private Intent view(Uri uri, String mimeType) {
        return new Intent(Intent.ACTION_VIEW)
                .setDataAndType(uri, mimeType);
    }

    private void start(Intent intent) {
        activity.startActivity(intent);
    }
}
