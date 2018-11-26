package org.steamzone.shaked.app.device

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.trello.rxlifecycle2.components.support.RxFragment
import org.steamzone.shaked.R
import kotlinx.android.synthetic.main.fragment_date_time_settings.*
import java.util.*
import java.util.Arrays.asList
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_button_led_settings.*
import org.steamzone.shaked.bt.new_settings_item
import org.steamzone.shaked.bt.old.BTClient
import org.steamzone.shaked.services.BTService
import java.text.SimpleDateFormat
import java.util.Arrays.asList


class DateTimeSettingsFragment : RxFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_date_time_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentTime = Calendar.getInstance().getTime()
        val plants = arrayOf(toSimpleString(currentTime))
        val aa = ArrayAdapter(context, android.R.layout.simple_spinner_item, plants)

        aa.setDropDownViewResource(android.R.layout.simple_spinner_item)
        date_interval_spinner.adapter = aa
        loadData()
    }

    fun toSimpleString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("dd-MM-yyy").format(this)
    }

    var settings: new_settings_item = new_settings_item()
    var dialog: ProgressDialog? = null
    @SuppressLint("CheckResult")
    private fun loadData() {
        if (dialog != null) {
            dialog?.dismiss()
        }
        dialog = ProgressDialog.show(activity, "",
                "Loading. Please wait...", true)

        if ((activity as DeviceActivity)?.isConnected() && BTService.deviceConnectedMap[(activity as DeviceActivity)?.deviceMac] != null) {

            BTService.deviceConnectedMap[(activity as DeviceActivity)?.deviceMac]?.readCharacteristic(BTClient.CHAR_LOGGER_SETTINGS_UUID)
                    ?.compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        Logger.wtf("Read SETTINGS!")
                        settings.parse_data(it)
                        setData(settings)
                        dialog?.dismiss()
                    }, {
                        dialog?.dismiss()
                        it.printStackTrace()
                    })


        } else {
            dialog?.dismiss()
        }


    }

    private fun setData(settings: new_settings_item) {
        device_time_text.text = "UNKNOWN"
        device_date_text.text = "UNKNOWN"
    }

    @SuppressLint("CheckResult")
    private fun sendBtSettingsToDevice() {

        if ((activity as DeviceActivity)?.isConnected() && BTService.deviceConnectedMap[(activity as DeviceActivity)?.deviceMac] != null) {

            var settings: new_settings_item = getBTSettingsUI()

            (activity as DeviceActivity)?.connectionBle?.writeCharacteristic(BTClient.CHAR_LOGGER_SETTINGS_UUID, settings.create_settings_bt_buff())
                    ?.compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        Snackbar.make(activity?.findViewById<View>(android.R.id.content)!!, getString(R.string.settings_writed), Snackbar.LENGTH_SHORT).show()
                    }, {
                        Snackbar.make(activity?.findViewById<View>(android.R.id.content)!!, "Error: " + it.message, Snackbar.LENGTH_SHORT).show()

                        it.printStackTrace()
                    })


        }


    }

    private fun getBTSettingsUI(): new_settings_item {


        return settings
    }

    fun Boolean.toInt() = if (this) 1 else 0


}
