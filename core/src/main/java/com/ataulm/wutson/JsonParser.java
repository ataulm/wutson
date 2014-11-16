package com.ataulm.wutson;

public interface JsonParser<T> {

    T parse(String json);

}
