package com.ataulm.wutson.repository.persistence;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import static com.ataulm.wutson.repository.persistence.DatabaseTable.CONFIGURATION;

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
        // TODO: this should be a timestamp as the first param
        ContentValues contentValues = ConfigurationColumn.write(0, json);
        contentResolver.insert(CONFIGURATION.uri(), contentValues);
    }

}
