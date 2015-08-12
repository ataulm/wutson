package com.ataulm.wutson.search;

public interface SearchSuggestion {

    String getName();

    Type getType();

    enum Type {
        HISTORY,
        API
    }

}
