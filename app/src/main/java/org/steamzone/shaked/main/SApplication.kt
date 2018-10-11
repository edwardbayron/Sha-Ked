package org.steamzone.shaked.app

import android.app.Application
import androidx.annotation.NonNull
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.steamzone.shaked.BuildConfig
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import org.steamzone.shaked.box.MyObjectBox


class SApplication() : Application() {

    companion object {
        lateinit var instance: SApplication
    }

    init {
        instance = this
    }

    private var boxStore: BoxStore? = null

    override fun onCreate() {
        super.onCreate()
        setupDB()
        setupLogger()
    }

    private fun setupDB() {
        boxStore = MyObjectBox.builder().androidContext(this)
                //.debugFlags(DebugFlags.LOG_QUERY_PARAMETERS)
                .buildDefault()
        if (BuildConfig.DEBUG) {
            AndroidObjectBrowser(boxStore).start(this)
        }

    }

    private fun setupLogger() {
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }


}