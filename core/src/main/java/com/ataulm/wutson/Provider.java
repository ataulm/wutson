package com.ataulm.wutson;

public interface Provider<T> {

    /**
     * Provides an instance of T.
     * <p/>
     * This may be a long operation - it's performed on the same thread that it's called on.
     */
    T provide();

}
