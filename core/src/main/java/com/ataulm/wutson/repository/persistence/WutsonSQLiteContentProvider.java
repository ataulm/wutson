package com.ataulm.wutson.repository.persistence;

import novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl;

public final class WutsonSQLiteContentProvider extends SQLiteContentProviderImpl {

    // TODO: this is core, but the authority is in mobile/AndroidManifest: someone's leaking.
    static final String AUTHORITY = "content://" + "com.ataulm.wutson2" + "/";

}
