package com.ataulm.wutson.repository.persistence;

import android.content.ContentValues;
import android.database.Cursor;

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

    static String readTmdbShowIdFrom(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(TMDB_SHOW_ID.columnName()));
    }

    String columnName() {
        return name().toLowerCase(Locale.UK);
    }

}
