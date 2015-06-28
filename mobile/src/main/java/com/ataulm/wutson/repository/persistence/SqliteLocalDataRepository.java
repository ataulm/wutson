package com.ataulm.wutson.repository.persistence;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.ataulm.wutson.shows.ShowId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ataulm.wutson.repository.persistence.DatabaseTable.*;

public class SqliteLocalDataRepository implements LocalDataRepository {

    private final ContentResolver contentResolver;

    public SqliteLocalDataRepository(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    public String readJsonConfiguration() {
        Cursor cursor = contentResolver.query(CONFIGURATION.uri(), null, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return "";
        }
        String json = ConfigurationColumn.readJsonFrom(cursor);
        cursor.close();
        return json;
    }

    @Override
    public void writeJsonConfiguration(String json) {
        ContentValues contentValues = ConfigurationColumn.write(Timestamp.now().asLong(), json);
        contentResolver.insert(CONFIGURATION.uri(), contentValues);
    }

    @Override
    public String readJsonGenres() {
        Cursor cursor = contentResolver.query(GENRES.uri(), null, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return "";
        }
        String json = GenresColumn.readJsonFrom(cursor);
        cursor.close();
        return json;
    }

    @Override
    public void writeJsonGenres(String json) {
        ContentValues contentValues = GenresColumn.write(Timestamp.now().asLong(), json);
        contentResolver.insert(GENRES.uri(), contentValues);
    }

    @Override
    public String readJsonShowSummaries(String tmdbGenreId) {
        String[] projection = {ShowSummariesColumn.JSON.columnName()};
        String selection = ShowSummariesColumn.TMDB_GENRE_ID.columnName() + "=?";
        String[] selectionArgs = {tmdbGenreId};
        Cursor cursor = contentResolver.query(SHOW_SUMMARIES.uri(), projection, selection, selectionArgs, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            return "";
        }
        String json = ShowSummariesColumn.readJsonFrom(cursor);
        cursor.close();
        return json;
    }

    @Override
    public void writeJsonShowSummary(String tmdbGenreId, String json) {
        ContentValues contentValues = ShowSummariesColumn.write(Timestamp.now().asLong(), tmdbGenreId, json);
        contentResolver.insert(SHOW_SUMMARIES.uri(), contentValues);
    }

    @Override
    public boolean isShowTracked(ShowId tmdbShowId) {
        String selection = TrackedShowsColumn.TMDB_SHOW_ID.columnName() + "=?";
        String[] selectionArgs = {tmdbShowId.toString()};
        Cursor cursor = contentResolver.query(TRACKED_SHOWS.uri(), null, selection, selectionArgs, null);
        boolean showIsTracked = cursor.getCount() > 0;
        cursor.close();
        return showIsTracked;
    }

    @Override
    public void addToTrackedShows(ShowId tmdbShowId) {
        ContentValues contentValues = TrackedShowsColumn.write(Timestamp.now().asLong(), tmdbShowId);
        contentResolver.insert(TRACKED_SHOWS.uri(), contentValues);
    }

    @Override
    public int deleteFromTrackedShows(ShowId tmdbShowId) {
        String selection = TrackedShowsColumn.TMDB_SHOW_ID.columnName() + "=?";
        String[] selectionArgs = {tmdbShowId.toString()};
        return contentResolver.delete(TRACKED_SHOWS.uri(), selection, selectionArgs);
    }

    @Override
    public String readJsonSeason(ShowId tmdbShowId, int seasonNumber) {
        String[] projection = {SeasonColumn.JSON.columnName()};
        String selection = SeasonColumn.TMDB_SHOW_ID.columnName() + "=? AND " + SeasonColumn.SEASON_NUMBER.columnName() + "=?";
        String[] selectionArgs = {tmdbShowId.toString(), String.valueOf(seasonNumber)};
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
    public void writeJsonSeason(ShowId tmdbShowId, int seasonNumber, String json) {
        ContentValues contentValues = SeasonColumn.write(Timestamp.now().asLong(), tmdbShowId.toString(), seasonNumber, json);
        contentResolver.insert(SEASONS.uri(), contentValues);
    }

    @Override
    public String readJsonShowDetails(ShowId tmdbShowId) {
        String[] projection = {ShowDetailsColumn.JSON.columnName()};
        String selection = ShowDetailsColumn.TMDB_SHOW_ID.columnName() + "=?";
        String[] selectionArgs = {tmdbShowId.toString()};
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
    public void writeJsonShowDetails(ShowId tmdbShowId, String json) {
        ContentValues contentValues = ShowDetailsColumn.write(Timestamp.now().asLong(), tmdbShowId.toString(), json);
        contentResolver.insert(SHOW_DETAILS.uri(), contentValues);
    }

    @Override
    public List<ShowId> getListOfTmdbShowIdsFromAllTrackedShows() {
        String[] projection = {TrackedShowsColumn.TMDB_SHOW_ID.columnName()};
        Cursor cursor = contentResolver.query(TRACKED_SHOWS.uri(), projection, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return Collections.emptyList();
        }

        List<ShowId> showIds = new ArrayList<>();
        do {
            showIds.add(TrackedShowsColumn.readTmdbShowIdFrom(cursor));
        } while (cursor.moveToNext());
        cursor.close();
        return showIds;
    }

}
