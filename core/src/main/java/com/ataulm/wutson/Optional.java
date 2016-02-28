package com.ataulm.wutson;

public final class Optional<T> {

    @SuppressWarnings("unchecked")
    private static final Optional ABSENT = new Optional(null);

    private final T data;

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> absent() {
        return ABSENT;
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> from(T data) {
        if (data == null) {
            return ABSENT;
        }
        return new Optional<>(data);
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> of(T data) {
        if (data == null) {
            throw DeveloperError.because("Data cannot be null. Use Optional.from(maybeNullData).");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Optional<?> optional = (Optional<?>) o;

        if (data != null ? !data.equals(optional.data) : optional.data != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }

}
