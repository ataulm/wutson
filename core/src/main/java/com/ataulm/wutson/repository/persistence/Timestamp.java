package com.ataulm.wutson.repository.persistence;

import java.util.concurrent.TimeUnit;

public class Timestamp {

    private final long millis;

    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    public Timestamp(long millis) {
        this.millis = millis;
    }

    long asMillis() {
        return millis;
    }

    public long differenceInHours(Timestamp other) {
        long otherMillis = other.asMillis();
        long difference = otherMillis - asMillis();
        return TimeUnit.MILLISECONDS.toHours(Math.abs(difference));
    }

}
