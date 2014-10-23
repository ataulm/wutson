package com.ataulm.mystories;

public interface JsonParser<T> {

    T parse(String json);

}
