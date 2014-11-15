package com.ataulm.watson;

public interface JsonParser<T> {

    T parse(String json);

}
