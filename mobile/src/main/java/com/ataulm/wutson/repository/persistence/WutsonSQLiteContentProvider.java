package com.ataulm.wutson.repository.persistence;

import novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl;

public final class WutsonSQLiteContentProvider extends SQLiteContentProviderImpl {

    static final String AUTHORITY = "content://" + "com.ataulm.wutson2" + "/";

}
