package com.ataulm.wutson.auth;

class AccessToken {

    private final String token;
    private final long expiryInSecondsSinceEpoch;

    public static AccessToken from(TraktOAuthTokenResponse response) {
        String accessToken = response.accessToken;
        long expiry = response.createdDateInSecondsSinceEpoch + response.accessTokenExpiryInSecondsSinceCreatedDate;
        return new AccessToken(accessToken, expiry);
    }

    public AccessToken(String token, long expiryInSecondsSinceEpoch) {
        this.token = token;
        this.expiryInSecondsSinceEpoch = expiryInSecondsSinceEpoch;
    }

    public boolean isEmpty() {
        return token == null || token.isEmpty();
    }

    public long getExpiry() {
        return expiryInSecondsSinceEpoch;
    }

    @Override
    public String toString() {
        return token;
    }

}
