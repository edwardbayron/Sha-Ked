package org.steamzone.shaked.app

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxrelay2.PublishRelay
import com.orhanobut.logger.Logger
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.scan.ScanSettings
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.content_main.*
import me.aflak.bluetooth.BluetoothCallback
import org.steamzone.shaked.R
import org.steamzone.shaked.box.DeviceBox
import java.util.concurrent.TimeUnit

open class SActivity : RxAppCompatActivity() {

    var scanResultNotConnectedBehaviorSubject: PublishRelay<MutableCollection<DeviceBox>> = PublishRelay.create()
    var scanResultConnectedBehaviorSubject: PublishRelay<MutableCollection<DeviceBox>> = PublishRelay.create()
    var scanMapConnected: HashMap<String, DeviceBox> = HashMap()
    var scanNotMapConnected: HashMap<String, DeviceBox> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindBTLifeCycle()
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
        scanSubscription?.dispose()
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
        Logger.wtf(SApplication.instance.rxBleClient.state.name)
        if (SApplication.instance.rxBleClient.state == RxBleClient.State.BLUETOOTH_NOT_ENABLED) {
            SApplication.instance.bluetooth.showEnableDialog(this@SActivity)
        } else if (SApplication.instance.rxBleClient.state == RxBleClient.State.READY) {

            initScanning()
        }

    }

    var scanSubscription: Disposable? = null

    private fun initScanning() {


        Logger.wtf("Yolo?")
        scanSubscription = SApplication.instance.rxBleClient.scanBleDevices(
                ScanSettings.Builder()
                        // .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
                        // .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
                        .build()
                // add filters if needed
        )
                .compose(bindUntilEvent(ActivityEvent.PAUSE))
                .buffer(10000, TimeUnit.MILLISECONDS)
                .subscribe(
                        { scanResults ->
                            for (scanResult in scanResults) {
                                if (scanResult.bleDevice.connectionState == RxBleConnection.RxBleConnectionState.CONNECTED) {
                                    scanMapConnected[scanResult.bleDevice.macAddress] = DeviceBox.createDevivceBoxFromScanResult(scanResult)
                                } else {
                                    scanNotMapConnected[scanResult.bleDevice.macAddress] = DeviceBox.createDevivceBoxFromScanResult(scanResult)
                                }
                                Logger.wtf("LOL: " + scanResult.bleDevice.name + ", add: " + scanResult.bleDevice.connectionState)
                            }

                            Logger.wtf("z: " + scanMapConnected.values.size)
                            scanResultNotConnectedBehaviorSubject.accept(scanNotMapConnected.values)
                            scanResultConnectedBehaviorSubject.accept(scanMapConnected.values)

                        },
                        { throwable ->
                            throwable.printStackTrace()
                        }
                )


        //scanSubscription.dispose()
    }


    override fun onStart() {
        super.onStart()
        SApplication.instance.bluetooth.onStart()
    }

    override fun onStop() {
        super.onStop()
        if (scanSubscription != null) {
            scanSubscription?.dispose()
        }
        SApplication.instance.bluetooth.onStop()
    }


}