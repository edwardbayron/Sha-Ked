package org.steamzone.shaked.rest.auth;

import android.os.Build;

import org.steamzone.shaked.BuildConfig;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationRequestInterceptor implements Interceptor {

    private String userName;
    private String password;


    public AuthenticationRequestInterceptor(String userName, String password) {
        this.userName = userName;
        this.password = password;

    }


    public static String getUserAgent() {
        return "ShaKed/" + BuildConfig.VERSION_NAME + " (Android; " + Build.MODEL + "; " + Build.VERSION.SDK_INT + ")";
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder().addHeader("UserBox-Agent", getUserAgent());

//        requestBuilder.addHeader("Accept-Encoding","gzip");
        // requestBuilder.addHeader("Content-Encoding", "gzip");

        if (password != null && userName != null) {
            String credits = Credentials.basic(userName, password);
            requestBuilder.addHeader("Authorization", credits);
        }

        return chain.proceed(requestBuilder.build());
    }
}