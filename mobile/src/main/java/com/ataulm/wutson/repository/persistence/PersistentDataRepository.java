package com.ataulm.wutson.repository.persistence;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import static com.ataulm.wutson.repository.persistence.DatabaseTable.CONFIGURATION;
import static com.ataulm.wutson.repository.persistence.DatabaseTable.GENRES;

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

}
