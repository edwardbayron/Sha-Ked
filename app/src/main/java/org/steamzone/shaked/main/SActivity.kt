package org.steamzone.shaked.app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxrelay2.PublishRelay
import com.orhanobut.logger.Logger
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.exceptions.BleScanException
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.content_main.*
import me.aflak.bluetooth.BluetoothCallback
import org.steamzone.shaked.R
import org.steamzone.shaked.box.DeviceBox
import org.steamzone.shaked.box.LoginBox
import org.steamzone.shaked.services.BTService
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.LinkedHashMap


open class SActivity : RxAppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun bindBTLifeCycle() {

        SApplication.instance.bluetooth.setBluetoothCallback(object : BluetoothCallback {
            override fun onUserDeniedActivation() {
                SApplication.instance.bluetooth.showEnableDialog(this@SActivity)
            }

            override fun onBluetoothOff() {
                Logger.wtf("BT OFF")
                SApplication.instance.bluetooth.showEnableDialog(this@SActivity)
            }

            override fun onBluetoothOn() {
                Logger.wtf("GOGOGO!")
            }

            override fun onBluetoothTurningOn() {
                Logger.wtf("BT Oning")
            }

            override fun onBluetoothTurningOff() {
                Logger.wtf("BT OFFinf")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        setupPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    @SuppressLint("CheckResult")
    private fun setupPermissions() {
        RxPermissions(this)
                .request(Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(
                        {
                            if (it) {
                                checkBTE()

                            } else {
                                Snackbar.make(root_view, R.string.no_permission, Snackbar.LENGTH_LONG).show()
                            }
                        },
                        {
                            it.printStackTrace()
                        }
                )
    }

    private fun checkBTE() {

        if (SApplication.instance.rxBleClient.state == RxBleClient.State.BLUETOOTH_NOT_ENABLED) {

            SApplication.instance.bluetooth.showEnableDialog(this@SActivity)

        } else if (SApplication.instance.rxBleClient.state == RxBleClient.State.READY) {
            if (LoginBox.get() != null) {
                Logger.wtf("START SERVICE")
                startService(Intent(this, BTService::class.java))
            }
        }

    }


    override fun onStart() {
        super.onStart()
        bindBTLifeCycle()
        try {
            SApplication.instance.bluetooth.onStart()
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun onPause() {
        try {
            SApplication.instance.bluetooth.onStop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onPause()

    }

    override fun onStop() {
        super.onStop()
        SApplication.instance.bluetooth.setBluetoothCallback(null)
    }


}