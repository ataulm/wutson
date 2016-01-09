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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Event<?> event = (Event<?>) o;

        if (type != event.type) {
            return false;
        }
        if (!data.equals(event.data)) {
            return false;
        }
        if (!error.equals(event.error)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + data.hashCode();
        result = 31 * result + error.hashCode();
        return result;
    }

    public enum Type {

        LOADING,
        ERROR,
        IDLE

    }

}
