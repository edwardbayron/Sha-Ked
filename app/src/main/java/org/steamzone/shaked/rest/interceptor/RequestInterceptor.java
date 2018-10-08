package org.steamzone.shaked.rest.interceptor;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

//        SettingsBox settingsBox = SettingsBox.get();
//        if (settingsBox.wifiOnly && NetworkUtility.isMobileDataEnabled()) {
//            return new Response.Builder().code(418).build();
//        }


        return chain.proceed(original);
    }
}
