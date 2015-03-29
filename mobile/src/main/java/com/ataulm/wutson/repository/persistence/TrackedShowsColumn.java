package com.ataulm.wutson.repository.persistence;

import android.content.ContentValues;

import java.util.Locale;

enum TrackedShowsColumn {

    CREATED,
    TMDB_SHOW_ID;

    static ContentValues write(long updatedTimestamp, String tmdbShowId) {
        ContentValues contentValues = new ContentValues(values().length);
        contentValues.put(CREATED.columnName(), updatedTimestamp);
        contentValues.put(TMDB_SHOW_ID.columnName(), tmdbShowId);
        return contentValues;
    }

    String columnName() {
        return name().toLowerCase(Locale.UK);
    }

}
