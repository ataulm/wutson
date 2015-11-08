package com.ataulm.wutson.auth;

import org.json.JSONException;
import org.json.JSONObject;

public final class TraktOAuthTokenResponse {

    public final String accessToken;
    public final long createdDateInSecondsSinceEpoch;
    public final long accessTokenExpiryInSecondsSinceCreatedDate;
    public final String refreshToken;

    public static TraktOAuthTokenResponse from(String json) {
        JSONObject response = jsonObjectFrom(json);

        String accessToken = getString(response, "access_token");
        String refreshToken = getString(response, "refresh_token");
        long expiresIn = getLong(response, "expires_in");
        long createdDate = getLong(response, "created_at");

        return new TraktOAuthTokenResponse(accessToken, createdDate, expiresIn, refreshToken);
    }

    private static String getString(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getString(key);
        } catch (JSONException e) {
            throw new RuntimeException("Error getting key " + key + " in " + jsonObject, e);
        }
    }

    private static long getLong(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getLong(key);
        } catch (JSONException e) {
            throw new RuntimeException("Error getting key " + key + " in " + jsonObject, e);
        }
    }

    private static JSONObject jsonObjectFrom(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            throw new RuntimeException("Unable to parse json response", e);
        }
    }

    private TraktOAuthTokenResponse(String accessToken, long createdDateInSecondsSinceEpoch, long accessTokenExpiryInSecondsSinceCreatedDate, String refreshToken) {
        this.accessToken = accessToken;
        this.createdDateInSecondsSinceEpoch = createdDateInSecondsSinceEpoch;
        this.accessTokenExpiryInSecondsSinceCreatedDate = accessTokenExpiryInSecondsSinceCreatedDate;
        this.refreshToken = refreshToken;
    }

}
