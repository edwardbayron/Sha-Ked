package org.steamzone.shaked.app.device

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.orhanobut.logger.Logger
import com.polidea.rxandroidble2.RxBleConnection
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_device.*
import kotlinx.android.synthetic.main.fragment_device_settings.*
import org.steamzone.shaked.R
import org.steamzone.shaked.box.SettingsBox
import org.steamzone.shaked.bt.new_settings_item
import org.steamzone.shaked.bt.old.BTClient
import java.util.*

class DeviceSettingsFragment : RxFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gps_settings_cont.setOnClickListener {

            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.hide(this)
                    ?.add(R.id.fragment_container, GPSSettingsFragment(), GPSSettingsFragment::class.java.name)
                    ?.addToBackStack(GPSSettingsFragment::class.java.name)
                    ?.commit()

        }

        bt_settings_cont.setOnClickListener {

            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.hide(this)
                    ?.add(R.id.fragment_container, BTSettingsFragment(), BTSettingsFragment::class.java.name)
                    ?.addToBackStack(BTSettingsFragment::class.java.name)
                    ?.commit()

        }

        //EVENT settings FRAGMENT
        event_settings_cont.setOnClickListener {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.hide(this)
                    ?.add(R.id.fragment_container, EventsSettingsFragment(), EventsSettingsFragment::class.java.name)
                    ?.addToBackStack(EventsSettingsFragment::class.java.name)
                    ?.commit()
        }

        button_led_settings_cont.setOnClickListener {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.hide(this)
                    ?.add(R.id.fragment_container, ButtonLEDSettingsFragment(), ButtonLEDSettingsFragment::class.java.name)
                    ?.addToBackStack(ButtonLEDSettingsFragment::class.java.name)
                    ?.commit()
        }

        checkDBSettings()
        showSettingsDataDate(SettingsBox.get())
        data_time_settings_cont.setOnClickListener {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.hide(this)
                    ?.add(R.id.fragment_container, DateTimeSettingsFragment(), DateTimeSettingsFragment::class.java.name)
                    ?.addToBackStack(DateTimeSettingsFragment::class.java.name)
                    ?.commit()
            get_bt_settings.setOnClickListener {
                readBTSettings()
            }
        }


        logger_settings_cont.setOnClickListener {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.hide(this)
                    ?.add(R.id.fragment_container, LoggerSettingsFragment(), LoggerSettingsFragment::class.java.name)
                    ?.addToBackStack(LoggerSettingsFragment::class.java.name)
                    ?.commit()
        }

        consumption_calculator_cont.setOnClickListener {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.hide(this)
                    ?.add(R.id.fragment_container, ConsumptionCalculatorFragment(), ConsumptionCalculatorFragment::class.java.name)
                    ?.addToBackStack(ConsumptionCalculatorFragment::class.java.name)
                    ?.commit()
        }

        firmware_update_cont.setOnClickListener {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.hide(this)
                    ?.add(R.id.fragment_container, FirmwareUpdateFragment(), FirmwareUpdateFragment::class.java.name)
                    ?.addToBackStack(FirmwareUpdateFragment::class.java.name)
                    ?.commit()
        }



    }

    var settings: new_settings_item = new_settings_item()
    private fun checkDBSettings() {
        settings = SettingsBox.converBoxToSettings(SettingsBox.get())
    }


    var readBtSettings: Disposable? = null
    private fun readBTSettings() {
        if (readBtSettings != null) {
            readBtSettings?.dispose()
            readBtSettings = null
        }

        if ((activity as DeviceActivity).checkConnectionStatus()) {

            readBtSettings = (activity as DeviceActivity)?.connectionBle?.readCharacteristic(BTClient.CHAR_LOGGER_SETTINGS_UUID)
                    ?.compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    ?.subscribe({
                        settings.parse_data(it)
                        var box = SettingsBox.dataRead(settings)
                        box.dateRead = Date()
                        SettingsBox.save(box)
                        showSettingsDataDate(box)

                    }, {
                        it.printStackTrace()
                    })

        }


    }

    @SuppressLint("SetTextI18n")
    private fun showSettingsDataDate(box: SettingsBox?) {
        if (box?.dateRead != null) {
            bt_settings_status_read_text.text = getString(R.string.last_read) + " " + box.dateRead.toString()
        } else {
            bt_settings_status_read_text.text = getString(R.string.last_read) + " null"
        }


        if (box?.dateWrite != null) {
            bt_settings_status_write_text.text = getString(org.steamzone.shaked.R.string.last_write) + " " + box.dateWrite.toString()
        } else {
            bt_settings_status_write_text.text = getString(org.steamzone.shaked.R.string.last_write) + " null"
        }


    }
}