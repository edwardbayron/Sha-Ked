package org.steamzone.shaked.app.device

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rx.ReplayingShare
import com.polidea.rxandroidble2.RxBleConnection
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_device_get_log.*
import org.steamzone.shaked.R
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
        connectionObservable = prepareConnectionObservable()
        setupListener()


    }

    private fun setupListener() {

        download_bt.setOnClickListener {
            startDownloading()

        }

    }

    private fun prepareConnectionObservable(): Observable<RxBleConnection> {
        return (activity as DeviceActivity)?.bleDevice
                ?.establishConnection(false)
                ?.takeUntil(disconnectTriggerSubject)
                ?.compose(bindUntilEvent<RxBleConnection>(FragmentEvent.PAUSE))
                ?.compose(ReplayingShare.instance())!!
    }

    @SuppressLint("CheckResult")
    private fun startDownloading() {

        connectionObservable.flatMap{
            t: RxBleConnection -> t.setupNotification(CHARACTERESTIC_DATA_TRANSFER_UUID)

        }       .doOnNext { activity?.runOnUiThread(this::notificationHasBeenSetUp) }
                .flatMap { t: Observable<ByteArray> -> t }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNotificationReceived, this::onNotificationSetupFailure)
//                .doOnNext(notificationObservable -> runOnUiThread(this::notificationHasBeenSetUp) )
//        .flatMap(notificationObservable -> notificationObservable)
//        .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::onNotificationReceived, this::onNotificationSetupFailure)
//




    }

    private fun notificationHasBeenSetUp() {

        Snackbar.make(activity?.findViewById<View>(android.R.id.content)!!, "Notifications has been set up", Snackbar.LENGTH_SHORT).show()
    }

    private fun onNotificationReceived(bytes: ByteArray) {

        Snackbar.make(activity?.findViewById<View>(android.R.id.content)!!, "Change: " + HexString.bytesToHex(bytes), Snackbar.LENGTH_SHORT).show()
    }

    private fun onNotificationSetupFailure(throwable: Throwable) {

        throwable.printStackTrace()
        Snackbar.make(activity?.findViewById<View>(android.R.id.content)!!, "Notifications error: ${throwable.localizedMessage}", Snackbar.LENGTH_SHORT).show()
    }

}