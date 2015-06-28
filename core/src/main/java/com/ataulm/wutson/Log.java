package com.ataulm.wutson;

public interface Log {

    void verbose(String tag, String message);

    void debug(String tag, String message);

    void error(String tag, String message);

    void error(String tag, String message, Throwable throwable);

}
