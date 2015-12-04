package com.ataulm.wutson.auth;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.HttpURLConnection;

public class AddAuthorizationHeaderInterceptor implements Interceptor {

    private final WutsonAccountManager accountManager;

    public AddAuthorizationHeaderInterceptor(WutsonAccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + accountManager.getAccessToken().toString())
                .build();

        Response response = chain.proceed(request);
        if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
            accountManager.invalidateAccessToken();
        }

        return response;
    }

}
