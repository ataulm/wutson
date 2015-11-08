package com.ataulm.wutson.auth;

import com.ataulm.wutson.BuildConfig;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import rx.Observable;
import rx.functions.Func1;

class TraktOAuthTokenRequester {

    public Observable<TraktOAuthTokenResponse> getAccessTokenInExchangeFor(String code) {
        return Observable.just("code=" + code)
                .map(getAccessToken("grant_type=authorization_code"));
    }

    private static Func1<String, TraktOAuthTokenResponse> getAccessToken(final String grantType) {
        return new Func1<String, TraktOAuthTokenResponse>() {

            @Override
            public TraktOAuthTokenResponse call(String token) {
                try {
                    MediaType textMediaType = MediaType.parse("application/x-www-form-urlencoded");
                    Request request = new Request.Builder()
                            .url("https://api-v2launch.trakt.tv/oauth/token")
                            .post(RequestBody.create(textMediaType, buildTokenRequestBody(grantType, token)))
                            .build();

                    Response response = new OkHttpClient().newCall(request).execute();
                    String result = response.body().string();

                    return TraktOAuthTokenResponse.from(result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        };
    }

    private static String buildTokenRequestBody(String grantType, String token) {
        return grantType +
                "&" + token +
                "&client_id=" + BuildConfig.TRAKT_API_KEY +
                "&client_secret=" + BuildConfig.TRAKT_API_SECRET +
                "&redirect_uri=" + BuildConfig.TRAKT_REDIRECT_URI;
    }

    public Observable<TraktOAuthTokenResponse> getAccessTokenInExchangeFor(RefreshToken refreshToken) {
        return Observable.just("refresh_token=" + refreshToken.toString())
                .map(getAccessToken("grant_type=refresh_token"));
    }

}
