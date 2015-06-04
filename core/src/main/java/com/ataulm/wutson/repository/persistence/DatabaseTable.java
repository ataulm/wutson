package com.ataulm.wutson.repository.persistence;

import android.net.Uri;

import java.util.Locale;

enum DatabaseTable {

    CONFIGURATION,
    GENRES,
    SHOW_SUMMARIES,
    TRACKED_SHOWS,
    SHOW_DETAILS,
    SEASONS;

    Uri uri() {
        return Uri.parse(WutsonSQLiteContentProvider.AUTHORITY).buildUpon().appendPath(tableName()).build();
    }

    private String tableName() {
        return name().toLowerCase(Locale.UK);
    }

}
