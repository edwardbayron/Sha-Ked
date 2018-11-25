package org.steamzone.shaked.app

import android.app.Application
import androidx.annotation.NonNull
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.steamzone.shaked.BuildConfig
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import org.steamzone.shaked.box.MyObjectBox
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.internal.RxBleLog
import me.aflak.bluetooth.Bluetooth




class SApplication() : Application() {

    companion object {
        lateinit var instance: SApplication
    }

    init {
        instance = this
    }

    private var boxStore: BoxStore? = null
    lateinit var rxBleClient: RxBleClient
    lateinit var bluetooth:Bluetooth
    override fun onCreate() {
        super.onCreate()
        setupBTE()
        setupDB()
        setupLogger()
    }

    private fun setupBTE() {
        rxBleClient = RxBleClient.create(this)
       // RxBle.init(getApplicationContext())
        //RxBle.enableLog(true)
        ///RxBle.markState()


        RxBleClient.setLogLevel(RxBleLog.VERBOSE)
        bluetooth = Bluetooth(this)
    }

    private fun setupDB() {
        Logger.wtf("BUILD DB")
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