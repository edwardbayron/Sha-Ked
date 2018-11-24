package org.steamzone.shaked.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.jakewharton.rxrelay2.PublishRelay
import com.orhanobut.logger.Logger
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.exceptions.BleScanException
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import com.trello.rxlifecycle2.android.ActivityEvent
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.steamzone.shaked.app.SApplication
import org.steamzone.shaked.box.DeviceBox
import org.steamzone.shaked.events.onConnectEvent
import org.steamzone.shaked.utils.JsonUtil
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.LinkedHashMap

class BTService : Service() {

    companion object {
        var isBtServiceRunning = false
        var scanResultBehaviorSubject: PublishRelay<MutableCollection<DeviceBox>> = PublishRelay.create()
        var scanMap: LinkedHashMap<String, DeviceBox> = LinkedHashMap()
        var deviceConnectedMap: LinkedHashMap<String, RxBleConnection?> = LinkedHashMap()
        var connectonCompositeDisposable: CompositeDisposable = CompositeDisposable()
        var connectonStatusCompositeDisposable: CompositeDisposable = CompositeDisposable()
        var connectonStatusSubscriptionMap: LinkedHashMap<String, Disposable?> = LinkedHashMap()


    }


    private lateinit var deviceBoxSubscription: DataSubscription
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.wtf("OLO?")

        return START_STICKY
    }


    override fun onCreate() {
        super.onCreate()
        Logger.wtf("YOLO")
        reCreateDispociables()
        isBtServiceRunning = true
        EventBus.getDefault().register(this)
        startBtScan()
        setupDeviceListener()
        startConnection()


    }

    private fun startConnection() {
        //handleDevices(DeviceBox.getAll())
    }

    private fun reCreateDispociables() {
        connectonCompositeDisposable = CompositeDisposable()
        connectonStatusCompositeDisposable = CompositeDisposable()

    }

    override fun onDestroy() {
        super.onDestroy()
        isBtServiceRunning = false
        EventBus.getDefault().unregister(this)
        scanSubscription?.dispose()
        deviceBoxSubscription.cancel()
        connectonStatusCompositeDisposable.clear()
        connectonCompositeDisposable.clear()

    }

    private var scanSubscription: Disposable? = null
    private fun startBtScan() {
        scanSubscription = SApplication.instance.rxBleClient.scanBleDevices(
                ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
                        .build()
        )

                .doFinally(this::dispose)
                .buffer(2000, TimeUnit.MILLISECONDS)
                .subscribe(this::addScanResult, this::onScanFailure)


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

    private fun dispose() {
        if (scanSubscription != null) {
            scanSubscription?.dispose()
            scanSubscription = null
        }

        scanMap.clear()
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


    private fun setupDeviceListener() {

        deviceBoxSubscription = DeviceBox.query().build().subscribe().observer(deviceBoxObserver)

    }


    private var deviceBoxObserver: DataObserver<List<DeviceBox>> = DataObserver<List<DeviceBox>> { data -> handleDevices(data) }

    private fun handleDevices(data: List<DeviceBox>) {
        Logger.wtf("DATA HAS CHANGED: " + data.size)
        for (deviceBox in data) {
            handleDeviceBox(deviceBox)
        }

    }

    private fun handleDeviceBox(deviceBox: DeviceBox) {
        Logger.wtf(JsonUtil.gson.toJson(deviceBox))
        if (deviceBox.autoConnect && !deviceBox.connected
        ) {
            connectDevice(deviceBox)

        } else if (!deviceBox.autoConnect && deviceBox.connected) {

            disconnectDevice(deviceBox)
        }


    }

    private fun disconnectDevice(deviceBox: DeviceBox) {

    }

    private fun connectDevice(deviceBox: DeviceBox) {
        Logger.wtf("CONENCT")
        if (connectonStatusSubscriptionMap[deviceBox.hardwareId!!] == null) {
            Logger.wtf("CONENCT 2")
            var bleDevice = deviceBox.hardwareId?.let {
                SApplication.instance.rxBleClient.getBleDevice(it)
            }


            var subscription = bleDevice?.observeConnectionStateChanges()
                    ?.subscribe({
                        deviceBox.connectionStatus = it.name
                        if (it === RxBleConnection.RxBleConnectionState.CONNECTED) {
                            deviceBox.connected = true
                        } else if (it === RxBleConnection.RxBleConnectionState.DISCONNECTED) {
                            deviceBox.connected = false
                        }
                        DeviceBox.save(deviceBox)

                    }, {
                        it.printStackTrace()
                    })

            connectonStatusCompositeDisposable.add(subscription!!)
            connectonStatusSubscriptionMap[deviceBox.hardwareId!!] = subscription


            if (bleDevice?.connectionState === RxBleConnection.RxBleConnectionState.CONNECTED) {
                triggerConnect(deviceBox, bleDevice)
            }
        }


    }

    private fun triggerConnect(deviceBox: DeviceBox, bleDevice: RxBleDevice) {
        if (deviceConnectedMap[deviceBox.hardwareId!!] == null) {
            Logger.wtf("Connect please")
            var connectionDisposable = bleDevice?.establishConnection(true)
//                ?.flatMap { rxBleConnection ->
//                    // Set desired interval.
//                    Observable.interval(2, TimeUnit.SECONDS).flatMapSingle{t: Long -> Single.just(rxBleConnection) }
//
//
//                }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        deviceBox.connected = true
                        deviceBox.connectionStatus = bleDevice.connectionState.name
                        DeviceBox.save(deviceBox)
                        deviceConnectedMap[deviceBox.hardwareId!!] = it
                    }, {

                    })

            connectonCompositeDisposable.add(connectionDisposable!!)
        }
    }


    @Subscribe
    public fun onSomeEventComeIn(onConnectEvent: onConnectEvent) {

    }


}