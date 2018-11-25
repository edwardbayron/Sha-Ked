package org.steamzone.shaked.app.device

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.logger.Logger
import com.polidea.rxandroidble2.RxBleConnection
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_device_get_log.*
import org.steamzone.shaked.R
import org.steamzone.shaked.bt.new_log_item
import org.steamzone.shaked.services.BTService
import org.steamzone.shaked.utils.HexString
import java.util.*


class DeviceGetLogFragment : RxFragment() {

    val CHARACTERESTIC_DATA_TRANSFER_UUID = UUID.fromString("F364ADDD-00B0-4240-BA50-05CA45Bf8ABC")
    val SERVICE_GPS_ONLINE_DATA_UUID = UUID.fromString("F3641400-00B0-4240-BA50-05CA45BF8ABC")

    lateinit var connectionObservable: Observable<RxBleConnection>

    private val disconnectTriggerSubject = PublishSubject.create<Boolean>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_get_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //connectionObservable = prepareConnectionObservable()
        setupListener()


    }

    private fun setupListener() {

        download_bt.setOnClickListener {
            startDownloading()

        }
        download_bt_save.setOnClickListener {
            writeDataToSDCard()
        }

    }

//    private fun prepareConnectionObservable(): Observable<RxBleConnection> {
//        return (activity as DeviceActivity)?.connectionBle
//                ?.establishConnection(false)
//                ?.takeUntil(disconnectTriggerSubject)
//                ?.compose(bindUntilEvent<RxBleConnection>(FragmentEvent.PAUSE))
//                ?.compose(ReplayingShare.instance())!!
//    }

    @SuppressLint("CheckResult")
    private fun startDownloading() {
        if ((activity as DeviceActivity)?.isConnected() && (activity as DeviceActivity)?.deviceMac != null) {
            Observable.just(BTService.deviceConnectedMap[(activity as DeviceActivity)?.deviceMac])
                    ?.compose(bindUntilEvent<RxBleConnection>(FragmentEvent.DESTROY_VIEW))
                    // ?.compose(ReplayingShare.instance())!!
                    //  .takeUntil(disconnectTriggerSubject)
                    ?.flatMap { t: RxBleConnection ->
                        t.setupNotification(CHARACTERESTIC_DATA_TRANSFER_UUID)
                    }
                    ?.doOnNext { activity?.runOnUiThread(this::notificationHasBeenSetUp) }
                    ?.flatMap { t: Observable<ByteArray> -> t }
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(this::onNotificationReceived, this::onNotificationSetupFailure) {
                        Logger.wtf("Finalized")
                        writeDataToSDCard()
                    }

        } else {
            Snackbar.make(activity?.findViewById<View>(android.R.id.content)!!, getString(R.string.device_not_connected), Snackbar.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("CheckResult")
    private fun writeDataToSDCard() {
        RxPermissions(this)
                .request(Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe({
                    if (it) {
                        logger_flash_data?.convert_data(activity, (activity as DeviceActivity)?.deviceMac)
                        Snackbar.make(activity?.findViewById<View>(android.R.id.content)!!, "Saved to internal space.", Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(root_view, R.string.no_permission, Snackbar.LENGTH_LONG).show()
                    }
                }, {
                    it.printStackTrace()
                })


    }

    private fun notificationHasBeenSetUp() {

        Snackbar.make(activity?.findViewById<View>(android.R.id.content)!!, "Notifications has been set up", Snackbar.LENGTH_SHORT).show()
    }

    var logger_flash_data: new_log_item = new_log_item()

    private fun onNotificationReceived(bytes: ByteArray) {
        logger_flash_data?.download_f(bytes)

        setSpeed()
        setSize()
        Snackbar.make(activity?.findViewById<View>(android.R.id.content)!!, "Change: " + HexString.bytesToHex(bytes), Snackbar.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    private fun setSize() {
        logger_flash_data?.let {
            downloading_text.text= "" + it.get_val_frame_download()+" "
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setSpeed() {
        logger_flash_data?.let {
            internet_speed.text = "" + it.instant_speed
        }
    }

    private fun onNotificationSetupFailure(throwable: Throwable) {

        throwable.printStackTrace()
        Snackbar.make(activity?.findViewById<View>(android.R.id.content)!!, "Notifications error: ${throwable.localizedMessage}", Snackbar.LENGTH_SHORT).show()
    }

}