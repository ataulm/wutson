package com.ataulm.wutson.repository.persistence;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Locale;

enum SeasonColumn {

    CREATED,
    TMDB_SHOW_ID,
    SEASON_NUMBER,
    JSON;

    static ContentValues write(long updatedTimestamp, String tmdbShowId, int seasonNumber, String showDetailsJson) {
        ContentValues contentValues = new ContentValues(values().length);
        contentValues.put(CREATED.columnName(), updatedTimestamp);
        contentValues.put(TMDB_SHOW_ID.columnName(), tmdbShowId);
        contentValues.put(SEASON_NUMBER.columnName(), seasonNumber);
        contentValues.put(JSON.columnName(), showDetailsJson);
        return contentValues;
    }

    static long readCreatedFrom(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndex(CREATED.columnName()));
    }

    static String readJsonFrom(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(JSON.columnName()));
    }

    String columnName() {
        return name().toLowerCase(Locale.UK);
    }

}
