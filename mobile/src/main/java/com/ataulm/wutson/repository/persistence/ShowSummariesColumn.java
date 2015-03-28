package com.ataulm.wutson.repository.persistence;

import android.content.ContentValues;

import java.util.Locale;

enum ShowSummariesColumn {

    CREATED,
    TMDB_GENRE_ID,
    JSON;

    static ContentValues write(long updatedTimestamp, String tmdbGenreId, String configurationJson) {
        ContentValues contentValues = new ContentValues(values().length);
        contentValues.put(CREATED.columnName(), updatedTimestamp);
        contentValues.put(TMDB_GENRE_ID.columnName(), tmdbGenreId);
        contentValues.put(JSON.columnName(), configurationJson);
        return contentValues;
    }

    static long readCreatedFrom(ContentValues contentValues) {
        return contentValues.getAsLong(CREATED.columnName());
    }

    static String readTmdbGenreIdFrom(ContentValues contentValues) {
        return contentValues.getAsString(TMDB_GENRE_ID.columnName());
    }

    static String readJsonFrom(ContentValues contentValues) {
        return contentValues.getAsString(JSON.columnName());
    }

    private String columnName() {
        return name().toLowerCase(Locale.UK);
    }

}
