package org.steamzone.shaked.global

import org.steamzone.shaked.BuildConfig

import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by sands on 22.04.2017.
 */

object GlobalConstants {

    val SERVER_SSL_URL = "https://user.magav300.com/webresources/"

    var REST_LOG_LEVEL: HttpLoggingInterceptor.Level = if (DebugConstants.NETWORK_LOG) HttpLoggingInterceptor.Level.values()[BuildConfig.LOG_LEVEL] else HttpLoggingInterceptor.Level.values()[0]
}
