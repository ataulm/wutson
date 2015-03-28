package com.ataulm.wutson.repository.persistence;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Locale;

enum GenresColumn {

    CREATED,
    JSON;

    static ContentValues write(long updatedTimestamp, String configurationJson) {
        ContentValues contentValues = new ContentValues(values().length);
        contentValues.put(CREATED.columnName(), updatedTimestamp);
        contentValues.put(JSON.columnName(), configurationJson);
        return contentValues;
    }

    static long readCreatedFrom(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndex(CREATED.columnName()));
    }

    static String readJsonFrom(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(JSON.columnName()));
    }

    private String columnName() {
        return name().toLowerCase(Locale.UK);
    }

}
