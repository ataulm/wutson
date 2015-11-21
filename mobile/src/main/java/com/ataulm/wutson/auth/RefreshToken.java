package com.ataulm.wutson.auth;

class RefreshToken {

    public static final RefreshToken EMPTY = new RefreshToken(null);

    private final String token;

    public RefreshToken(String token) {
        this.token = token;
    }

    public boolean isEmpty() {
        return token == null || token.isEmpty();
    }

    @Override
    public String toString() {
        return token;
    }

}
