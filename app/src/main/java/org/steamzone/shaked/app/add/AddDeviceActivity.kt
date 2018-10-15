package org.steamzone.shaked.app.add

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
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


class AddDeviceActivity : SActivity() {

    var adapterBt: MainBTListAdapter? = null

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

        var scanSubjectSubscanSubjectSub = scanResultNotConnectedBehaviorSubject.observeOn(AndroidSchedulers.mainThread()).subscribe(
                {


                    adapterBt?.setupList(it.toList())
                },
                {
                    it.printStackTrace()
                }
        )

    }


    private fun initList() {
        var viewManager = LinearLayoutManager(this)
        adapterBt = MainBTListAdapter(object : MainBTViewHolder.OnItemClickListener {
            override fun onItemClick(item: DeviceBox) {

                connectToDevice(item)

            }

        })

        adapterBt?.setupList(scanNotMapConnected.values.toList())

        add_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = adapterBt
        }

    }

    private fun connectToDevice(item: DeviceBox) {

        val device = item.hardwareId?.let { SApplication.instance.rxBleClient.getBleDevice(it) }

        val disposable = device?.establishConnection(false) // <-- autoConnect flag
                ?.subscribe(
                        {

                            finish()
                        },
                        { throwable ->
                            throwable.printStackTrace()
                        }
                )
    }


}
