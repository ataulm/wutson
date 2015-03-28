package com.ataulm.wutson.repository.persistence;

import com.ataulm.wutson.BuildConfig;

import novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl;

public final class PersistentDataRepository extends SQLiteContentProviderImpl {

    static final String AUTHORITY = "content://" + BuildConfig.APPLICATION_ID + "/";

}
