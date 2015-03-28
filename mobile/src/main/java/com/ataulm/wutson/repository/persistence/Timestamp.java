package com.ataulm.wutson.repository.persistence;

public class Timestamp {

    private final long millis;

    static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    private Timestamp(long millis) {
        this.millis = millis;
    }

    long asLong() {
        return millis;
    }

}
