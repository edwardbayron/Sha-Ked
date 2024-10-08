package org.steamzone.shaked.app.device

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.logger.Logger
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_device.*
import org.steamzone.shaked.R
import org.steamzone.shaked.app.SActivity
import org.steamzone.shaked.app.SApplication
import org.steamzone.shaked.box.DeviceBox
import org.steamzone.shaked.box.SettingsBox
import org.steamzone.shaked.bt.new_settings_item
import org.steamzone.shaked.bt.old.BTClient
import org.steamzone.shaked.services.BTService
import java.util.*
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


        openMainFragment()


    }

    private fun openMainFragment() {
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, DeviceFragment(), DeviceFragment::class.java.name)
                // .addToBackStack(null)
                .commit()
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

        updateConnectionButton(null)
        if (isConnected()) {
            triggerConnect()
        }
    }

    fun isConnected(): Boolean {
        return bleDevice?.connectionState === RxBleConnection.RxBleConnectionState.CONNECTED
    }

    fun onConnectionStateChange(newState: RxBleConnection.RxBleConnectionState) {
        Logger.wtf("STATE change: " + newState.name)
        Snackbar.make(findViewById<View>(android.R.id.content), "STATE: " + newState.name, Snackbar.LENGTH_SHORT).show()
        connect_bt.text = newState.name

        updateConnectionButton(newState.name)

    }

    private fun updateConnectionButton(state: String?) {
        connect_bt.isEnabled = true
        if (state != null) {
            connect_bt.text = state
        } else {
            if (isConnected()) {
                connect_bt.setText(R.string.connected)

            } else {
                connect_bt.setText(R.string.disconnected)
            }
        }
    }

    private fun setupClickListeners() {

        back_icon.setOnClickListener {
            this@DeviceActivity.onBackPressed()
        }

//        connect_bt.setOnClickListener {
//            disconnectOrConnect()
//        }

    }


    private fun disconnectOrConnect() {

        if (isConnected()) {
            triggerDisconnect()
        } else {
            triggerConnect()
        }


    }

    private fun triggerConnect() {

        BTService.deviceConnectedMap[deviceMac]?.let {
            onConnectionReceived(it)
        }

    }

    private fun onConnectionFailure(throwable: Throwable) {

        Snackbar.make(findViewById<View>(android.R.id.content), "Connection error: $throwable", Snackbar.LENGTH_SHORT).show()
    }


    private fun onConnectionReceived(connection: RxBleConnection) {

        updateStatusDataOfDevice(connection)


        Logger.wtf("Connection statius")
        //TODO do stuff here with connection?

        // Snackbar.make(findViewById<View>(android.R.id.content), "Connection received", Snackbar.LENGTH_SHORT).show()

       // getSettings()

    }

    var settings: new_settings_item = new_settings_item()


    @SuppressLint("CheckResult")
    private fun getSettings() {

        connectionBle?.readCharacteristic(BTClient.CHAR_LOGGER_SETTINGS_UUID)
                ?.compose(bindUntilEvent(ActivityEvent.DESTROY))
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    settings.parse_data(it)
                    var box = SettingsBox.dataRead(settings)
                    box.dateRead = Date()
                    SettingsBox.save(box)
                    Logger.wtf("REad settings done")

                }, {
                    it.printStackTrace()
                })
    }

    var connectionBle: RxBleConnection? = null
    @SuppressLint("CheckResult")
    private fun updateStatusDataOfDevice(connection: RxBleConnection) {
        connectionBle = connection
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


    }

    private fun triggerDisconnect() {

        if (connectionDisposable != null && !connectionDisposable?.isDisposed!!) {
            connectionDisposable?.dispose()
        }
    }


    fun checkConnectionStatus(): Boolean {

        return if (isConnected()) {
            true
        } else {
            Snackbar.make(findViewById<View>(android.R.id.content)!!, getString(R.string.device_not_connected), Snackbar.LENGTH_SHORT).show()
            false
        }
    }

}



