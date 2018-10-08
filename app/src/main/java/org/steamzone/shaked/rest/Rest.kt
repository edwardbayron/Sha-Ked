package org.steamzone.shaked.rest


import org.steamzone.shaked.global.GlobalConstants
import org.steamzone.shaked.rest.api.API
import org.steamzone.shaked.rest.auth.AuthenticationRequestInterceptor
import org.steamzone.shaked.rest.error.RxErrorHandlingCallAdapterFactory
import org.steamzone.shaked.rest.interceptor.RequestInterceptor

import java.util.concurrent.TimeUnit


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class Rest {


    val restAdapter: Retrofit
        get() = getRestAdapter(null, null, null)



    @JvmOverloads
    fun getRestAdapter(userName: String?, password: String?, url: String? = null): Retrofit {
        val restBuilder = Retrofit.Builder()
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.connectTimeout(260, TimeUnit.SECONDS) //connect
        clientBuilder.writeTimeout(260, TimeUnit.SECONDS)
        clientBuilder.readTimeout(260, TimeUnit.SECONDS) //socket

        clientBuilder.addInterceptor(AuthenticationRequestInterceptor(userName, password))
        clientBuilder.addInterceptor(RequestInterceptor())

        // add response interceptors after logging
        clientBuilder.addInterceptor { chain ->
            val original = chain.proceed(chain.request())
            if (original.code() == 209 || original.code() == 303) {
                 original.newBuilder().code(409).build()
            } else if (original.code() == 0) {
                original.newBuilder().code(409).build()
            } else {
                original
            }
        }
        // add request interceptors before logging
        clientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(GlobalConstants.REST_LOG_LEVEL))


        if (url != null) {
            restBuilder.baseUrl(url)
        } else {
            restBuilder.baseUrl(GlobalConstants.SERVER_SSL_URL)
        }
        restBuilder.addConverterFactory(MoshiConverterFactory.create())
        restBuilder.addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
        restBuilder.client(clientBuilder.build())
        //  restBuilder.addCallAdapterFactory(RxJavaCallAdapterFactory.createDefault());

        return restBuilder.build()
    }

    companion object {
        fun create(): API {
            return Rest().restAdapter.create(API::class.java)
        }
        fun create(userName: String?, password:String?): API {
            return Rest().getRestAdapter(userName, password).create(API::class.java)
        }
        fun create(userName: String?, password:String?, url: String?): API {
            return Rest().getRestAdapter(userName, password, url).create(API::class.java)
        }
    }


}
