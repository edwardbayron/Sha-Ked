package org.steamzone.shaked.app

import android.Manifest
import android.annotation.SuppressLint
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
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.LinkedHashMap


open class SActivity : RxAppCompatActivity() {

    var scanResultBehaviorSubject: PublishRelay<MutableCollection<DeviceBox>> = PublishRelay.create()
    var scanMap: LinkedHashMap<String, DeviceBox> = LinkedHashMap()
    var tikCounter = 0


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

        if (SApplication.instance.rxBleClient.state == RxBleClient.State.BLUETOOTH_NOT_ENABLED) {
            SApplication.instance.bluetooth.showEnableDialog(this@SActivity)
        } else if (SApplication.instance.rxBleClient.state == RxBleClient.State.READY) {

            initScanning()
        }

    }

    private var scanSubscription: Disposable? = null

    private fun initScanning() {

        scanSubscription = SApplication.instance.rxBleClient.scanBleDevices(
                ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
                        .build()

        )
                .compose(bindUntilEvent(ActivityEvent.PAUSE))
                .doFinally(this::dispose)
                .buffer(3000, TimeUnit.MILLISECONDS)
                .subscribe(this::addScanResult, this::onScanFailure)


        //scanSubscription.dispose()
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

        if (scanSubscription != null) {
            scanSubscription?.dispose()
        }

    }

    private fun addScanResult(bleScanResults: List<ScanResult>) {

        for (bleScanResult in bleScanResults) {
            scanMap[bleScanResult.bleDevice.macAddress] = DeviceBox.createDevivceBoxFromScanResult(bleScanResult)


        }
        //convert map to a List
        val list = scanMap.toList()


        //sorting the list with a comparator
        // Collections.sort(list) { o1, o2 -> (o2?.second?.rssi?.compareTo(o1?.second?.rssi!!)!!) }
        val sortedList = list.sortedByDescending { it.second.rssi }  //list.sortedWith(compareBy {it.second.rssi})


        //convert sortedMap back to Map
        scanMap = LinkedHashMap()
        for (entry in sortedList) {
            scanMap[entry.first] = entry.second
        }



        scanResultBehaviorSubject.accept(scanMap.values)

    }


    private fun dispose() {
        if (scanSubscription != null) {
            scanSubscription?.dispose()
            scanSubscription = null
        }

        scanMap.clear()
    }


    private fun onScanFailure(throwable: Throwable) {

        if (throwable is BleScanException) {
            handleBleScanException(throwable)
        }
    }

    private fun handleBleScanException(bleScanException: BleScanException) {
        val text: String

        when (bleScanException.reason) {
            BleScanException.BLUETOOTH_NOT_AVAILABLE -> text = "Bluetooth is not available"
            BleScanException.BLUETOOTH_DISABLED -> text = "Enable bluetooth and try again"
            BleScanException.LOCATION_PERMISSION_MISSING -> text = "On Android 6.0 location permission is required. Implement Runtime Permissions"
            BleScanException.LOCATION_SERVICES_DISABLED -> text = "Location services needs to be enabled on Android 6.0"
            BleScanException.SCAN_FAILED_ALREADY_STARTED -> text = "Scan with the same filters is already started"
            BleScanException.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> text = "Failed to register application for bluetooth scan"
            BleScanException.SCAN_FAILED_FEATURE_UNSUPPORTED -> text = "Scan with specified parameters is not supported"
            BleScanException.SCAN_FAILED_INTERNAL_ERROR -> text = "Scan failed due to internal error"
            BleScanException.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES -> text = "Scan cannot start due to limited hardware resources"
            BleScanException.UNDOCUMENTED_SCAN_THROTTLE -> text = String.format(
                    Locale.getDefault(),
                    "Android 7+ does not allow more scans. Try in %d seconds",
                    secondsTill(bleScanException.retryDateSuggestion)
            )
            BleScanException.UNKNOWN_ERROR_CODE, BleScanException.BLUETOOTH_CANNOT_START -> text = "Unable to start scanning"
            else -> text = "Unable to start scanning"
        }
        Logger.e(text)
        Logger.e(bleScanException.toString())
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }


    fun secondsTill(retryDateSuggestion: Date?): Long {
        return TimeUnit.MILLISECONDS.toSeconds(retryDateSuggestion?.time!! - System.currentTimeMillis())
    }


}