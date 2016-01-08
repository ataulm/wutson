package com.ataulm.wutson.repository.event;

import com.ataulm.wutson.DeveloperError;

public final class Optional<T> {

    @SuppressWarnings("unchecked")
    private static final Optional ABSENT = new Optional(null);

    private final T data;

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> absent() {
        return ABSENT;
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> of(T data) {
        if (data == null) {
            return ABSENT;
        }
        return new Optional<>(data);
    }

    private Optional(T data) {
        this.data = data;
    }

    public boolean isPresent() {
        return data != null;
    }

    public T get() {
        if (!isPresent()) {
            throw DeveloperError.because("You must check if data is present before using get()");
        }
        return data;
    }

}
