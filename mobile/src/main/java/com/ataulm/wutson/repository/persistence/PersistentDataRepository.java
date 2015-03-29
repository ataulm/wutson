package com.ataulm.wutson.repository.persistence;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import static com.ataulm.wutson.repository.persistence.DatabaseTable.CONFIGURATION;
import static com.ataulm.wutson.repository.persistence.DatabaseTable.GENRES;
import static com.ataulm.wutson.repository.persistence.DatabaseTable.SHOW_SUMMARIES;
import static com.ataulm.wutson.repository.persistence.DatabaseTable.TRACKED_SHOWS;

public class PersistentDataRepository {

    private final ContentResolver contentResolver;

    public PersistentDataRepository(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public String readJsonConfiguration() {
        Cursor cursor = contentResolver.query(CONFIGURATION.uri(), null, null, null, null);
        if (!cursor.moveToFirst()) {
            return "";
        }
        String json = ConfigurationColumn.readJsonFrom(cursor);
        cursor.close();
        return json;
    }

    public void writeJsonConfiguration(String json) {
        ContentValues contentValues = ConfigurationColumn.write(Timestamp.now().asLong(), json);
        contentResolver.insert(CONFIGURATION.uri(), contentValues);
    }

    public String readJsonGenres() {
        Cursor cursor = contentResolver.query(GENRES.uri(), null, null, null, null);
        if (!cursor.moveToFirst()) {
            return "";
        }
        String json = GenresColumn.readJsonFrom(cursor);
        cursor.close();
        return json;
    }

    public void writeJsonGenres(String json) {
        ContentValues contentValues = GenresColumn.write(Timestamp.now().asLong(), json);
        contentResolver.insert(GENRES.uri(), contentValues);
    }

    public String readJsonShowSummaries(String tmdbGenreId) {
        String[] projection = {ShowSummariesColumn.JSON.columnName()};
        String selection = ShowSummariesColumn.TMDB_GENRE_ID.columnName() + "=?";
        String[] selectionArgs = {tmdbGenreId};
        Cursor cursor = contentResolver.query(SHOW_SUMMARIES.uri(), projection, selection, selectionArgs, null);

        if (!cursor.moveToFirst()) {
            return "";
        }
        String json = ShowSummariesColumn.readJsonFrom(cursor);
        cursor.close();
        return json;
    }

    public void writeJsonShowSummary(String tmdbGenreId, String json) {
        ContentValues contentValues = ShowSummariesColumn.write(Timestamp.now().asLong(), tmdbGenreId, json);
        contentResolver.insert(SHOW_SUMMARIES.uri(), contentValues);
    }

    public boolean isShowTracked(String tmdbShowId) {
        String selection = TrackedShowsColumn.TMDB_SHOW_ID.columnName() + "=?";
        String[] selectionArgs = {tmdbShowId};
        Cursor cursor = contentResolver.query(TRACKED_SHOWS.uri(), null, selection, selectionArgs, null);
        boolean showIsTracked = cursor.getCount() > 0;
        cursor.close();
        return showIsTracked;
    }

    public Uri addToTrackedShows(String tmdbShowId) {
        ContentValues contentValues = TrackedShowsColumn.write(Timestamp.now().asLong(), tmdbShowId);
        return contentResolver.insert(TRACKED_SHOWS.uri(), contentValues);
    }

    public int deleteFromTrackedShows(String tmdbShowId) {
        String selection = TrackedShowsColumn.TMDB_SHOW_ID.columnName() + "=?";
        String[] selectionArgs = {tmdbShowId};
        return contentResolver.delete(TRACKED_SHOWS.uri(), selection, selectionArgs);
    }

}
