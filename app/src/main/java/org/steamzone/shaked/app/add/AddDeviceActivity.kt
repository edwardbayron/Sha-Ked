package org.steamzone.shaked.app.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_add_device.*
import org.steamzone.shaked.R
import org.steamzone.shaked.app.SActivity
import org.steamzone.shaked.app.home.list.MainBTListAdapter
import org.steamzone.shaked.app.home.list.MainBTViewHolder
import org.steamzone.shaked.box.DeviceBox
import io.reactivex.disposables.Disposable
import com.polidea.rxandroidble2.RxBleDevice
import org.steamzone.shaked.app.SApplication
import org.steamzone.shaked.app.add.list.AddDeviceBTViewHolder
import org.steamzone.shaked.app.add.list.AddDeviceBtAdapter


class AddDeviceActivity : SActivity() {

    var adapterBt: AddDeviceBtAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_device)
        setupToolbar()
        initList()
        setupPublishListener()
    }

    private fun setupToolbar() {
        toolbar.setTitle(R.string.add_device)
//        setSupportActionBar(toolbar)

    }


    @SuppressLint("CheckResult")
    private fun setupPublishListener() {

        var scanSubjectSubscanSubjectSub = scanResultBehaviorSubject.observeOn(AndroidSchedulers.mainThread()).subscribe(
                {

                    checkList(it)

                },
                {
                    it.printStackTrace()
                }
        )

    }

    private fun checkList(list: MutableCollection<DeviceBox>?) {
        progress_list.visibility = View.GONE

        if (list?.size == adapterBt?.list!!.size) {

            for (i in adapterBt?.list!!.indices) {
                val deviceBox = adapterBt?.list!![i]

                for (scannedDevicesBoxes in list?.toList()!!) {

                    if (scannedDevicesBoxes.hardwareId.equals(deviceBox.hardwareId)) {
                        deviceBox.rssi = scannedDevicesBoxes.rssi
                        deviceBox.distance = scannedDevicesBoxes.distance
                        deviceBox.batteryInfo = scannedDevicesBoxes.batteryInfo
                        deviceBox.batteryInfoVoltage = scannedDevicesBoxes.batteryInfoVoltage
                        deviceBox.connected = scannedDevicesBoxes.connected
                        adapterBt?.notifyItemChanged(i)

                        break
                    }
                }


            }
        } else {
            adapterBt?.setupList(list?.toList()!!)
        }
    }


    private fun initList() {
        var viewManager = LinearLayoutManager(this)
        adapterBt = AddDeviceBtAdapter(object : AddDeviceBTViewHolder.OnItemClickListener {
            override fun onItemClick(item: DeviceBox) {

                addDevice(item)

            }

        })
        adapterBt?.setupList(scanMap.values.toList())

        add_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = adapterBt
        }

    }

    private fun addDevice(item: DeviceBox) {
        DeviceBox.save(item)
        finish()
    }


}
