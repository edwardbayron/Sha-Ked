package org.steamzone.shaked.app.device

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.logger.Logger
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_device.*
import org.steamzone.shaked.R
import org.steamzone.shaked.app.SActivity
import org.steamzone.shaked.app.SApplication
import org.steamzone.shaked.box.DeviceBox
import java.util.concurrent.TimeUnit


class DeviceActivity : SActivity() {
    companion object {
        val MAC_ADDRESS_EXTRA = "MAC_ADDRESS_EXTRA"
    }

    var deviceMac: String? = null
    var deviceBox: DeviceBox? = null
    var bleDevice: RxBleDevice? = null
    var connectionDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        deviceMac = intent.extras?.getString(MAC_ADDRESS_EXTRA, null)
        if (deviceMac == null) {
            finish()
        }
        deviceBox = DeviceBox.getDeviceBoxByMac(deviceMac)
        if (deviceBox == null) {
            finish()
        }
        setContentView(R.layout.activity_device)
        setupConnection()
        setupClickListeners()
        setupDeviceInfo()


    }

    private fun setupDeviceInfo() {
        device_name.text = deviceBox?.name
        mac_text.text = deviceBox?.hardwareId
        type_text.text = deviceBox?.type

    }

    @SuppressLint("CheckResult")
    private fun setupConnection() {
        bleDevice = deviceMac?.let { SApplication.instance.rxBleClient.getBleDevice(it) }

        bleDevice?.observeConnectionStateChanges()?.compose(bindUntilEvent(ActivityEvent.DESTROY))
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(this::onConnectionStateChange, this::onConnectionFailure)

        updateConnectionButton()
        if (isConnected()) {
            triggerConnect()
        }
    }

    private fun isConnected(): Boolean {
        return bleDevice?.connectionState === RxBleConnection.RxBleConnectionState.CONNECTED
    }

    fun onConnectionStateChange(newState: RxBleConnection.RxBleConnectionState) {
        Logger.wtf("STATE change: " + newState.name)
        Snackbar.make(findViewById<View>(android.R.id.content), "STATE: " + newState.name, Snackbar.LENGTH_SHORT).show()
        updateConnectionButton()

    }

    private fun updateConnectionButton() {
        connect_bt.isEnabled = true
        if (isConnected()) {
            connect_bt.setText(R.string.connected)

        } else {
            connect_bt.setText(R.string.disconnected)
        }
    }

    private fun setupClickListeners() {

        back_icon.setOnClickListener {
            this@DeviceActivity.finish()
        }

        connect_bt.setOnClickListener {
            disconnectOrConnect()
        }

    }


    private fun disconnectOrConnect() {

        if (isConnected()) {
            triggerDisconnect()
        } else {
            triggerConnect()
        }


    }

    private fun triggerConnect() {
        Logger.wtf("Connect please")
        connectionDisposable = bleDevice?.establishConnection(true)
                ?.compose(bindUntilEvent(ActivityEvent.PAUSE))
                ?.doFinally(this::dispose)
//                ?.flatMap { rxBleConnection ->
//                    // Set desired interval.
//                    Observable.interval(2, TimeUnit.SECONDS).flatMapSingle{t: Long -> Single.just(rxBleConnection) }
//
//
//                }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(this::onConnectionReceived, this::onConnectionFailure)
    }

    private fun onConnectionFailure(throwable: Throwable) {

        Snackbar.make(findViewById<View>(android.R.id.content), "Connection error: $throwable", Snackbar.LENGTH_SHORT).show()
    }


    private fun onConnectionReceived(connection: RxBleConnection) {

        updateStatusDataOfDevice(connection)


        Logger.wtf("Connection statius")
        //TODO do stuff here with connection?

        // Snackbar.make(findViewById<View>(android.R.id.content), "Connection received", Snackbar.LENGTH_SHORT).show()
    }

    @SuppressLint("CheckResult")
    private fun updateStatusDataOfDevice(connection: RxBleConnection) {


        connection.discoverServices()
                .toObservable()
                .take(1)
                .compose(bindUntilEvent(ActivityEvent.PAUSE))

                .subscribe({

                }, {
                    it.printStackTrace()
                })

        readRssi(connection)

    }

    var readRSSIDisposable: Disposable? = null
    private fun readRssi(connection: RxBleConnection) {
        readRSSIDisposable?.dispose()

        readRSSIDisposable = connection.readRssi()
                ?.compose(bindUntilEvent(ActivityEvent.PAUSE))
                ?.toObservable()
                ?.flatMap { t: Int ->
                    Observable.interval(2, TimeUnit.SECONDS)
                            .flatMapSingle { timer: Long -> connection.readRssi() }
                }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    showRssi(it)

                },
                        {
                            it.printStackTrace()
                        })

    }

    private fun showRssi(rssi: Int) {
        distance_text.text = "" + Math.floor(DeviceBox.calculateDistance(rssi)) + " M"
        rssi_text.text = "" + rssi + " dBM"
    }


    private fun dispose() {
        Logger.wtf("Disposed")
        connectionDisposable = null
        updateConnectionButton()


    }

    private fun triggerDisconnect() {

        if (connectionDisposable != null) {
            connectionDisposable?.dispose()
        }
    }


}



