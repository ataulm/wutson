package com.ataulm.wutson.model;

public class ShowId {

    private final String id;

    public ShowId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShowId showId = (ShowId) o;
        return id.equals(showId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
