package com.ataulm.wutson.repository.persistence;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Locale;

enum ShowPeopleColumn {

    CREATED,
    TRAKT_SHOW_ID,
    JSON;

    static ContentValues write(long updatedTimestamp, String traktShowId, String json) {
        ContentValues contentValues = new ContentValues(values().length);
        contentValues.put(CREATED.columnName(), updatedTimestamp);
        contentValues.put(TRAKT_SHOW_ID.columnName(), traktShowId);
        contentValues.put(JSON.columnName(), json);
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
