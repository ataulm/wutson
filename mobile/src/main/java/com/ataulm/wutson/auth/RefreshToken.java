package com.ataulm.wutson.auth;

class RefreshToken {

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
