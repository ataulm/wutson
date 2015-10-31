package com.ataulm.wutson.repository.persistence;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.ataulm.wutson.shows.ShowId;

import static com.ataulm.wutson.repository.persistence.DatabaseTable.*;

public class SqliteJsonRepository implements JsonRepository {

    private final ContentResolver contentResolver;

    public SqliteJsonRepository(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    public long readTrendingShowsCreatedDate() {
        String[] projection = {TrendingShowsColumn.CREATED.columnName()};
        Cursor cursor = contentResolver.query(TRENDING_SHOWS.uri(), projection, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return 0;
        }
        long json = TrendingShowsColumn.readCreatedFrom(cursor);
        cursor.close();
        return json;
    }

    @Override
    public String readTrendingShowsList() {
        String[] projection = {TrendingShowsColumn.JSON.columnName()};
        Cursor cursor = contentResolver.query(TRENDING_SHOWS.uri(), projection, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return "";
        }
        String json = TrendingShowsColumn.readJsonFrom(cursor);
        cursor.close();
        return json;
    }

    @Override
    public void writeTrendingShowsList(String json) {
        ContentValues contentValues = TrendingShowsColumn.write(Timestamp.now().asMillis(), json);
        contentResolver.insert(TRENDING_SHOWS.uri(), contentValues);
    }

    @Override
    public long readPopularShowsCreatedDate() {
        String[] projection = {PopularShowsColumn.CREATED.columnName()};
        Cursor cursor = contentResolver.query(POPULAR_SHOWS.uri(), projection, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return 0;
        }
        long json = PopularShowsColumn.readCreatedFrom(cursor);
        cursor.close();
        return json;
    }

    @Override
    public String readPopularShowsList() {
        String[] projection = {PopularShowsColumn.JSON.columnName()};
        Cursor cursor = contentResolver.query(POPULAR_SHOWS.uri(), projection, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return "";
        }
        String json = PopularShowsColumn.readJsonFrom(cursor);
        cursor.close();
        return json;
    }

    @Override
    public void writePopularShowsList(String json) {
        ContentValues contentValues = PopularShowsColumn.write(Timestamp.now().asMillis(), json);
        contentResolver.insert(POPULAR_SHOWS.uri(), contentValues);
    }

    @Override
    public String readShowDetails(ShowId showId) {
        String[] projection = {ShowDetailsColumn.JSON.columnName()};
        String selection = ShowDetailsColumn.TRAKT_SHOW_ID.columnName() + "=?";
        String[] selectionArgs = {showId.toString()};
        Cursor cursor = contentResolver.query(SHOW_DETAILS.uri(), projection, selection, selectionArgs, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return "";
        }
        String json = ShowDetailsColumn.readJsonFrom(cursor);
        cursor.close();
        return json;
    }

    @Override
    public void writeShowDetails(ShowId showId, String json) {
        ContentValues contentValues = ShowDetailsColumn.write(Timestamp.now().asMillis(), showId.toString(), json);
        contentResolver.insert(SHOW_DETAILS.uri(), contentValues);
    }

    @Override
    public String readSeasons(ShowId showId) {
        String[] projection = {SeasonColumn.JSON.columnName()};
        String selection = SeasonColumn.TRAKT_SHOW_ID.columnName() + "=?";
        String[] selectionArgs = {showId.toString()};
        Cursor cursor = contentResolver.query(SEASONS.uri(), projection, selection, selectionArgs, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return "";
        }
        String json = SeasonColumn.readJsonFrom(cursor);
        cursor.close();
        return json;
    }

    @Override
    public void writeSeasons(ShowId showId, String json) {
        ContentValues contentValues = SeasonColumn.write(Timestamp.now().asMillis(), showId.toString(), json);
        contentResolver.insert(SEASONS.uri(), contentValues);
    }

}
