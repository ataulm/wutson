package com.ataulm.wutson.navigation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.ataulm.wutson.discover.Show;

public class Navigator {

    private static final Uri URI_SHOW = Uri.parse("content://com.ataulm.wutson2/show");
    private static final String MIME_TYPE_SHOW_ITEM = "vnd.android.cursor.item/vnd.com.ataulm.wutson.show";

    private final Activity activity;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    public void to(Show show) {
        Uri uri = Uri.withAppendedPath(URI_SHOW, show.getId());
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(uri, MIME_TYPE_SHOW_ITEM);

        activity.startActivity(intent);
    }

}
