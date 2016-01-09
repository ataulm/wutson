package com.ataulm.wutson.repository.event;

import com.ataulm.wutson.DeveloperError;

public class Event<T> {

    private final Type type;
    private final Optional<T> data;
    private final Optional<Throwable> error;

    public static <T> Event<T> loading() {
        return loading(null);
    }

    public static <T> Event<T> loading(T data) {
        return new Event<>(Type.LOADING, Optional.of(data), Optional.<Throwable>absent());
    }

    public static <T> Event<T> idle() {
        return idle(null);
    }

    public static <T> Event<T> idle(T data) {
        return new Event<>(Type.IDLE, Optional.of(data), Optional.<Throwable>absent());
    }

    public static <T> Event<T> error(Throwable error) {
        return error(null, error);
    }

    public static <T> Event<T> error(T data, Throwable error) {
        if (error == null) {
            throw DeveloperError.because("Error events must contain an error.");
        }
        return new Event<>(Type.ERROR, Optional.of(data), Optional.of(error));
    }

    private Event(Type type, Optional<T> data, Optional<Throwable> error) {
        this.type = type;
        this.data = data;
        this.error = error;
    }

    public Type getType() {
        return type;
    }

    public Optional<T> getData() {
        return data;
    }

    public Optional<Throwable> getError() {
        return error;
    }

    public enum Type {

        LOADING,
        ERROR,
        IDLE

    }

}
