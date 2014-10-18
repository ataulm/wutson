package com.ataulm.mystories;

public interface Parser<T> {

    T parse(String json);

}
